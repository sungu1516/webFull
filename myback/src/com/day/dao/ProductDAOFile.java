package com.day.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.day.dto.Product;
import com.day.exception.FindException;

public class ProductDAOFile implements ProductDAO {
	private String fileName = "products.txt"; // 50001:�����ó�Ʈ:1000:200101
												// 50002:��������:1500:220311
												// 50003:������:1000:201105

	@Override
	public List<Product> selectAll() throws FindException {
		/*
		 * 1. ���� �б�(FileInputStream�� ������ Scanner ���)
		 * 2. ���ٳ����� ":" �����ڷ� 5�� ���ڿ��� �ڸ���(split)
		 * 3. ��ǰ��ȣ, ��ǰ���� ����/ ��ǰ������ int�� ��ȯ/ �������ڴ� Date�� ��ȯ
		 * 4. ��ǰ������ Product��ü�� ����
		 * 5. List�� Product��ü�� �߰�
		 */
		List<Product> list = new ArrayList<>();
		Scanner sc = null;

		try {

			sc = new Scanner(new FileInputStream(fileName));
			while (true) {
				String line = sc.nextLine(); // 50001:�����ó�Ʈ:1000:200101:have four cameras �� �����´�. �ٹٲ��� �������� ������
				String[] data = line.split(":", 5);

				String prod_no = data[0];
				String prod_name = data[1];
				int prod_price = -1;

				try {
					prod_price = Integer.parseInt(data[2]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

				Date prod_mf_df = new Date();
				try {
					prod_mf_df = new SimpleDateFormat("yyMMdd").parse(data[3]);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String prod_detail = data[4];

				Product p = new Product(prod_no, prod_name, prod_price, prod_mf_df, prod_detail);
				list.add(p);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FindException("������ ã�� �� �����ϴ�.");

		} catch (NoSuchElementException | IllegalStateException e) {
			// TODO: handle exception
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		return list;
	}

	@Override
	public List<Product> selectAll(int currentPage) throws FindException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product selectByNo(String prod_no) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> selectByName(String word) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		ProductDAO dao = new ProductDAOFile();
		try {
			List<Product> all = dao.selectAll();
			System.out.println(all);
		} catch (FindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
