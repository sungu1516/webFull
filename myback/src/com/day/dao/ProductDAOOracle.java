package com.day.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import com.day.dto.Product;
import com.day.exception.FindException;
import com.day.sql.Myconnection;

public class ProductDAOOracle implements ProductDAO {

	public ProductDAOOracle() throws Exception {
		// JDBC연결
		Class.forName("oracle.jdbc.driver.OracleDriver");
		System.out.println("JDB연결 성공");

	}

	@Override
	public List<Product> selectAll() throws FindException {
		// DB연결
		Connection con = null;

		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}

		String selectALLSQL = "SELECT * FROM product ORDER BY prod_no ASC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<>();

		try {
			pstmt = con.prepareStatement(selectALLSQL);
			rs = pstmt.executeQuery();

			while (rs.next()) { // 위 구문에 위해 결과행들을 가져왔다고 할 때, 첫 번째 행으로 이동. 한번 더 호출하면 그 다음 행으로 이동. 이동한 위치에 행이 존재할
								// 경우 true를 반환하고, 행이 없는 경우 false를 반환한다.
				// 행의 컬럼값 얻기
				String prod_no = rs.getString("prod_no"); // prod_no 열을 가져온다.
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");
				java.sql.Date prod_mf_dt = rs.getDate("prod_mfg_dt");

				Product p = new Product(prod_no, prod_name, prod_price, prod_mf_dt, null);
				list.add(p);
			}
			if (list.size() == 0) {
				throw new FindException("상품이 없습니다");

			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace(); // 콘솔에 예외의 종류와 내용, 줄번호까지 출력됨
			System.out.println("test");
			throw new FindException(e.getMessage()); // 강제적으로 예외 발생. FindException이라는, 사용자가 정의한 예외로 가공한다고 보면 됨.
			// 가공의 목적은 가독성을 높이기 위한 것. 왜 오류가 났는지 구체적으로 파악 가능
		} finally {
			// DB연결해제
			Myconnection.close(con, pstmt, rs);

		}

	}

	@Override
	public List<Product> selectAll(int currentPage) throws FindException {
		int cnt_per_page = 4; // page별 보여줄 목록 수
		Connection con = null;

		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<>();

		String selectAllPageSQL = "SELECT *\r\n" + "FROM (SELECT rownum rn, a.*\r\n" + "           FROM   (SELECT *\r\n"
				+ "                        FROM order_view \r\n"
				+ "                        --WHERE order_dt BETWEEN '21/01/01' AND '21/03/31' \r\n"
				+ "                        ORDER BY order_no DESC\r\n" + "                       ) a\r\n"
				+ "          )\r\n" + "WHERE rn BETWEEN START_ROW(?, ?) AND  END_ROW(?, ?)\r\n";

		try {
			pstmt = con.prepareStatement(selectAllPageSQL);
			pstmt.setInt(1, currentPage);
			pstmt.setInt(2, cnt_per_page);
			pstmt.setInt(3, currentPage);
			pstmt.setInt(4, cnt_per_page);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String prod_no = rs.getString("prod_no");
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");
				java.sql.Date prod_dt = rs.getDate("prod_mfg_dt");

				Product p = new Product(prod_no, prod_name, prod_price, prod_dt, null);
				list.add(p);

			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			Myconnection.close(con, pstmt, rs);
		}

	}

	@Override
	public Product selectByNo(String prod_no) throws FindException {
		Connection con = null;

		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String selectByNoSQL = "SELECT * FROM product WHERE prod_no = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Product p = null;

		try {
			pstmt = con.prepareStatement(selectByNoSQL);
			pstmt.setString(1, prod_no);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				String prod_no2 = rs.getString("prod_no");
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");
				java.sql.Date prod_dt = rs.getDate("prod_mfg_dt");
				p = new Product(prod_no2, prod_name, prod_price, prod_dt, null);
			} else { // 만약 rs.next() 가 false 라면 강제오류 발생
				throw new FindException("상품이 존재하지 않습니다");
			}

		}

		catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			Myconnection.close(con, pstmt, rs);
		}

		return p;

	}

	@Override
	public List<Product> selectByName(String word) throws FindException {
		Connection con = null;

		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}

		String selectByName = "SELECT * FROM product WHERE prod_name LIKE ? ORDER BY prod_no";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<>();

		try {
			pstmt = con.prepareStatement(selectByName);
			pstmt.setString(1, "%" + word + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String prod_no = rs.getString("prod_no");
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");
				java.sql.Date prod_dt = rs.getDate("prod_mfg_dt");
				Product p = new Product(prod_no, prod_name, prod_price, prod_dt, null);
				list.add(p);
			}
			
			if (list.size() == 0) {
				throw new FindException("해당 상품이 존재하지 않습니다");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			Myconnection.close(con, pstmt, rs);
		}
		
		return list;
	}

	public static void main(String[] args) {
//		try {
//			ProductDAOOracle dao = new ProductDAOOracle(); // 에러가 발생하면 부모 Exception 으로 넘어감
//			List<Product> all = dao.selectAll(); // 에러가 발생하면 FindException으로
//			for (Product p : all) {
//				System.out.println(p); // p.toString() 자동호출됨
//
//			}
//		}
//
//		catch (FindException e) {
//			System.out.println(e.getMessage()); // 사용자 입장에서 예외 메세지만 보도록 한다.
//		} catch (Exception e) { // 이와 같이 자식 Exception을 먼저 작성하고, 이후에 부모를 작성해야 compile 에러가 발생하지 않는다.
//			System.out.println(e.getMessage());
//		}

//		try {
//			ProductDAOOracle dao = new ProductDAOOracle(); // 에러가 발생하면 부모 Exception 으로 넘어감
//			Product p = dao.selectByNo("C0001"); // 에러가 발생하면 FindException으로
//			System.out.println(p);
//		}
//
//		catch (FindException e) {
//			System.out.println(e.getMessage()); // 사용자 입장에서 예외 메세지만 보도록 한다.
//
//		} catch (Exception e) { // 이와 같이 자식 Exception을 먼저 작성하고, 이후에 부모를 작성해야 compile 에러가 발생하지 않는다.
//			System.out.println(e.getMessage());
//		}
		
		String word = "냥";
		System.out.println("\"" + word + "\"" + "단어를 포함한 상품목록");
		
		try {
			ProductDAOOracle dao = new ProductDAOOracle(); // 에러가 발생하면 부모 Exception 으로 넘어감
			List<Product> list = dao.selectByName(word); // 에러가 발생하면 FindException으로
			System.out.println(list);
		}

		catch (FindException e) {
			System.out.println(e.getMessage()); // 사용자 입장에서 예외 메세지만 보도록 한다.

		} catch (Exception e) { // 이와 같이 자식 Exception을 먼저 작성하고, 이후에 부모를 작성해야 compile 에러가 발생하지 않는다.
			System.out.println(e.getMessage());
		}
		
		
	}

}
