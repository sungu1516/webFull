package com.day.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;
import javax.sound.sampled.Line;

import com.day.dto.Customer;
import com.day.dto.OrderInfo;
import com.day.dto.OrderLine;
import com.day.dto.Product;
import com.day.exception.AddException;
import com.day.exception.FindException;
import com.day.sql.Myconnection;

public class orderDAOOracle implements OrderDAO {

	@Override
	public void insert(OrderInfo info) throws AddException {
		Connection con = null;

		try {
			con = Myconnection.getConnection();
			con.setAutoCommit(false); // 자동커밋 해제
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}

		try {
			insertInfo(con, info);
			insertLines(con, info.getLines());
			con.commit(); // 커밋
		} catch (Exception e) {
			try {
				con.rollback(); // 롤백
			} catch (Exception e1) {
			}
			throw new AddException(e.getMessage());
		} finally {
			Myconnection.close(con, null, null);
		}
	}

	/**
	 * 주문 기본정보를 추가한다.
	 * 
	 * @param con  DB연결객체
	 * @param info 주문기본정보
	 * @throws AddException
	 */
	private void insertInfo(Connection con, OrderInfo info) throws AddException {
		// SQL송신
		PreparedStatement pstmt = null;
		String insertInfoSQL = "INSERT INTO order_info(order_no, order_id)" + " VALUES (ORDER_SEQ.NEXTVAL, ?)";

		try {
			pstmt = con.prepareStatement(insertInfoSQL);
			pstmt.setString(1, info.getOrder_c().getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException("주문기본추가실패: " + e.getMessage());
		} finally {
			Myconnection.close(null, pstmt, null); // con 은 연결 끊지 않는다.insert메서드에서 끊는다. rs는 애초에 만들지 않았으니 null.
		}
	}

	/**
	 * 주문 상세정보를 추가
	 * 
	 * @param con   DB연결객체
	 * @param lines 주문상세정보
	 * @throws AddException
	 */

	private void insertLines(Connection con, List<OrderLine> lines) throws AddException {
		PreparedStatement pstmt = null;
		String insertLinesSQL = "INSERT INTO order_line(order_no, order_prod_no, order_quantity)\r\n"
				+ "VALUES (ORDER_SEQ.CURRVAL, ?, ?)";

		try {
			pstmt = con.prepareStatement(insertLinesSQL);
			for (OrderLine line : lines) {
				pstmt.setString(1, line.getOrder_p().getProd_no());
				pstmt.setInt(2, line.getOrder_quantity());
				pstmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException("주문상세추가실패: " + e.getMessage());
		} finally {
			Myconnection.close(null, pstmt, null); // con 은 연결 끊지 않는다. rs는 애초에 만들지 않았으니 null.
		}
	}

	@Override
	public List<OrderInfo> selectById(String id) throws FindException {
		Connection con = null;

		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectByIdSQL = "SELECT oi.order_no, order_dt, order_prod_no,  prod_name, prod_price, order_quantity"
				+ "FROM order_info oi JOIN order_line ol ON(oi.order_no = ol.order_no)"
				+ "JOIN product p  ON (ol.order_prod_no = p.prod_no)" + "WHERE order_id = ?\r\n"
				+ "ORDER BY oi.order_no DESC, order_prod_no";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<OrderInfo> list = new ArrayList<>();
		
		try {
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				OrderInfo oi = new OrderInfo();

				String order_no = rs.getString("order_no");
				// order_c
				try {
					CustomerDAOOracle dao = new CustomerDAOOracle();
					Customer order_c = dao.selectById(id);
					oi.setOrder_c(order_c);
				} catch (Exception e) {
					e.printStackTrace();
					throw new FindException(e.getMessage());
				}
				java.sql.Date order_dt = rs.getDate("order_dt");
				// lines
				List<OrderLine> lines = new ArrayList<OrderLine>();

				for (int i = 1; i <= 3; i++) {
					OrderLine line = new OrderLine();
					Product p = new Product();
					p.setProd_no(rs.getString("order_prod_no"));
					line.setOrder_p(p);
					line.setOrder_quantity(rs.getInt("order_quantity"));
					lines.add(line);
				}

				oi.setOrder_no(order_no);
				oi.setOrder_dt(order_dt);
				oi.setLines(lines);

				list.add(oi);
			}
			if (list.size() == 0) {
				throw new FindException("주문내역이 없습니다");
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			Myconnection.close(con, pstmt, rs);
		}

	}

	public static void main(String[] args) {
		orderDAOOracle dao = new orderDAOOracle();
		OrderInfo info = new OrderInfo();
		Customer c = new Customer();
		c.setId("id1");
		info.setOrder_c(c);

		List<OrderLine> lines = new ArrayList<OrderLine>();
		for (int i = 1; i <= 3; i++) {
			OrderLine line = new OrderLine();
			Product p = new Product();
			p.setProd_no("C000" + i);
			line.setOrder_p(p);
			line.setOrder_quantity(i * 1000);
			lines.add(line);
		}
		info.setLines(lines);

		try {
			dao.insert(info);
		} catch (AddException e) {
			e.printStackTrace();
		}

	}

}
