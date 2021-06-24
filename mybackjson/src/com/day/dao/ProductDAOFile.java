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
	private String fileName = "products.txt"; // 50001:갤럭시노트:1000:200101
												// 50002:갤럭시탭:1500:220311
												// 50003:아이폰:1000:201105

	@Override
	public List<Product> selectAll() throws FindException {
		/*
		 * 1. 파일 읽기(FileInputStream을 가공한 Scanner 사용)
		 * 2. 한줄내용을 ":" 구분자로 5개 문자열로 자르기(split)
		 * 3. 상품번호, 상품명은 무관/ 상품가격은 int로 변환/ 제조일자는 Date로 변환
		 * 4. 상품정보를 Product객체로 생성
		 * 5. List에 Product객체를 추가
		 */
		List<Product> list = new ArrayList<>();
		Scanner sc = null;

		try {

			sc = new Scanner(new FileInputStream(fileName));
			while (true) {
				String line = sc.nextLine(); // 50001:갤럭시노트:1000:200101:have four cameras 를 가져온다. 줄바꿈을 기준으로 가져옴
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
			throw new FindException("파일을 찾을 수 없습니다.");

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
