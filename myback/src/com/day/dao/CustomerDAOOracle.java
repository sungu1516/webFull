package com.day.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.day.dto.Customer;
import com.day.dto.Product;
import com.day.exception.AddException;
import com.day.exception.FindException;
import com.day.exception.ModifyException;
import com.day.sql.Myconnection;

public class CustomerDAOOracle implements CustomerDAO {
	public CustomerDAOOracle() throws Exception {
		//JDBC드라이버로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("JDBC드라이버로드 성공");
	}
	
	@Override
	public void insert(Customer c) throws AddException {
		Connection con = null;
		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}
		
		PreparedStatement pstmt = null;
		String insertSQL = "INSERT INTO customer(id, pwd, name) VALUES(?, ?, ?)";
		
		try {
			pstmt = con.prepareStatement(insertSQL);
			pstmt.setString(1, c.getId());
			pstmt.setString(2, c.getPwd());
			pstmt.setString(3, c.getName());
			int rowcnt = pstmt.executeUpdate();
			if(rowcnt == 1) {
				System.out.println("가입 성공");
			} else {
				throw new AddException("가입 실패");
			}
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			Myconnection.close(con, pstmt, null);
		}

	}

	@Override
	public Customer selectById(String id) throws FindException {
		Connection con = null;
		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectByIdSQL = "SELECT * FROM customer WHERE id = ?";
		
		try {
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			Customer c = null;
			
			if(rs.next()/*== true*/) {
				//행의 컬럼 값 얻기
				String id1 = rs.getString("id");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String buildingno = rs.getString("buildingno");
				int enabled = rs.getInt("enabled");
				c = new Customer(id1, pwd, name, buildingno, enabled);
			} else {
				throw new FindException("아이디를 찾을 수 없습니다");
			}
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			//DB연결해제
			Myconnection.close(con, pstmt, rs);
		}
	}
	 
	@Override
	public void update(Customer c) throws ModifyException {
		Connection con = null;
		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		}
		
		//SQL구문 송신
		String updateSQL = 
				"UPDATE customer SET ";
		if(c.getEnabled() == 0) {
			updateSQL += "enabled = '0'";
					//'" + c.getid() + "'";
		} else if("".equals(c.getPwd()) && "".equals(c.getName()) && "".equals(c.getBuildingno())) {
			System.out.println("변경할 내용이 없습니다");
		} else if(!"".equals(c.getPwd()) && !"".equals(c.getName()) && "".equals(c.getBuildingno())) { //비번변경, 이름변경
			updateSQL += "pwd='" + c.getPwd() +"', name='" +c.getName() +"'";
		} else if(!"".equals(c.getPwd()) && "".equals(c.getName()) && !"".equals(c.getBuildingno())) { //비번변경, buildingno변경
			updateSQL += "pwd='" + c.getPwd() +"', buildingno='" +c.getBuildingno() +"'";
		} else if("".equals(c.getPwd()) && !"".equals(c.getName()) && !"".equals(c.getBuildingno())) { //이름변경, buildingno변경
			updateSQL += "name='" + c.getName() +"', buildingno='" +c.getBuildingno() +"'";
		} else if(!"".equals(c.getBuildingno())) { //buildingno만 변경
			updateSQL += "buildingno='" +c.getBuildingno() +"'";
		} else if(!"".equals(c.getName())) { //이름만 변경
			updateSQL += "name='" +c.getName() +"'";
		} else if(!"".equals(c.getPwd())) { //비번만 변경
			updateSQL += "pwd='" +c.getPwd() +"'";
		} else {
			updateSQL += "pwd='" + c.getPwd() +"', name='" +c.getName() +"'" +c.getBuildingno() +"'";
		}
		updateSQL += " WHERE id = ?";
		System.out.println(updateSQL);
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(updateSQL);
			pstmt.setString(1, c.getId());
			pstmt.executeUpdate();
			System.out.println(c.getId() + "고객의 내용이 변경되었습니다");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Myconnection.close(con, pstmt, null);
		}

	}

//	   @Override
//	   public void update(Customer c) throws ModifyException {
//
//	      Connection con = null;
//
//	      try {
//	         con = Myconnection.getConnection();
//	      } catch (SQLException e) {
//	         e.printStackTrace();
//	      }
//
//	      String updateSQL = "UPDATE customer SET ";
//	      if (c.getEnabled() == 0) {
//	         updateSQL += "enabled = '0'";
//
//	      } else {
//
//	         if (c.getpwd().equals("") && c.getname().equals("") && c.getbuildingno().equals("")) {
//	            System.out.println("변경할 내용이 없습니다");
//	            return;
//	         }
//
//	         if (!c.getpwd().equals("")) {
//	            updateSQL += "pwd = '" + c.getpwd() + "', ";
//	         }
//
//	         if (!c.getname().equals("")) {
//	            updateSQL += "name = '" + c.getname() + "', ";
//	         }
//
//	         if (!c.getbuildingno().equals("")) {
//	            updateSQL += "buildingno = '" + c.getbuildingno() + "', ";
//	         }
//
//	         updateSQL = updateSQL.substring(0, updateSQL.length() - 2); //공백 한 칸과 ,를 없애주기 위해!! 
//	      }
//
//	      updateSQL += " WHERE id ='" + c.getid() + "'";
//
//	      System.out.println(updateSQL);
//	      Statement stmt = null;
//
//	      try {
//	         stmt = con.createStatement();
//	         int rowcnt = stmt.executeUpdate(updateSQL); // 이름이 변경된 건수
//	         System.out.println("총 " + rowcnt + "건이 변경되었습니다.");
//
//	      } catch (SQLException e) {
//	         e.printStackTrace();
//	         throw new ModifyException(e.getMessage());
//	      } finally {
//	         Myconnection.close(con, stmt, null);
//	      }
//	   }
	
	/*
	*[강사님] flag로 변경할경우 SET절 변경할 속성 추가(쉼표처리!)
			String updateSQL = "UPDATE customer	SET ";
			String updateSQL2= "WHERE id = ?";
			boolean flag = false; //변경할 값이 있는 경우 true
			String pwd = c.getPwd();		
			if( pwd != null && !pwd.equals("")) {
				updateSQL += "pwd = '" + pwd + "'";
				flag = true;
			}		

			String name = c.getName();		
			if( name != null && !name.equals("")) {
				if(flag) {
					updateSQL += ",";
				}		
				updateSQL += "name = '" + name + "'";
				flag = true;
			}		

			String buildingno = c.getBuildingno();		
			if( buildingno != null && !buildingno.equals("")) {
				if(flag) {
					updateSQL += ",";
				}	
				updateSQL += "buildingno = '" + buildingno + "'";
				flag = true;
			}
			int enabled = c.getEnabled();		
			if( enabled > -1 ) { //0-탈퇴, 1-활동 
				if(flag) {
					updateSQL += ",";
				}
				updateSQL += "enabled = '" + enabled + "'";
				flag = true;
			}
			if(!flag) {
				throw new ModifyException("수정할 내용이 없습니다");
			}
			updateSQL += updateSQL2; //Where절 추가
	*/
	
	public static void main(String[] args) {
		//JDBC드라이버 로드
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("JDBC드라이버로드 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		Customer c = new Customer("id7", "qwer", "qwww", "12312421");
//		c.setid("qwer");
//		c.setpwd("qwer");
//		c.setbuildingno("qwer");
		try {
			CustomerDAOOracle dao = new CustomerDAOOracle();
			dao.insert(c);
		} catch (AddException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
//		//main - selectById
//		String id = "id1";
//		try {
//			CustomerDAOOracle dao = new CustomerDAOOracle();
//			Customer c = dao.selectById(id);
//				System.out.println(c);
//		} catch (FindException e) {
//			System.out.println(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
			
		
//		Customer c = new Customer("id8", "qwer", "11", "123");
//		c.setid("id8");
//		c.setpwd("qwer");
//		c.setbuildingno("11");
//		try {
//			CustomerDAOOracle dao = new CustomerDAOOracle();
//			dao.update(c);
//			System.out.println(c);
//		} catch (ModifyException e) {
//			System.out.println(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
				
	}
	
	
}
