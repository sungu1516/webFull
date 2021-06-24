package com.day.service;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import com.day.dao.CustomerDAO;
import com.day.dto.Customer;
import com.day.dto.Product;
import com.day.exception.AddException;
import com.day.exception.FindException;
import com.day.exception.ModifyException;

public class CustomerService {
	/*
	 * 가입 signup(c:Customer) : void
	 * 로그인 login(id:String, pwd:String) : void
	 * 정보 조회 detail(id:String) : Customer
	 * 정보 수정 modify(id:String) : void
	 * 탈퇴 leave(id:String) : void
	 */
	private CustomerDAO dao;
//	private static CustomerService service = new CustomerService();
	private static CustomerService service;
	public static String envProp; //
	private CustomerService() {
		Properties env = new Properties();
		try {
			//env.load(new FileInputStream("classes.prop"));
			env.load(new FileInputStream(envProp));
			String className = env.getProperty("customerDAO"); //classes.prop내에 지정되어 있는 CustomerDAO 경로
			Class c = Class.forName(className); //JVM에 로드
			dao = (CustomerDAO) c.newInstance(); //객체 생성
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static CustomerService getInstance() {
		if(service == null) {
			service = new CustomerService();
		}
		return service;
	}
	
	public void signup(Customer c) throws AddException {
		dao.insert(c);
	}
	
	public void login(String id, String pwd) throws FindException {
		System.out.println("test dao" + dao);
		Customer c = dao.selectById(id);
		System.out.println("로그인 성공");
		if(!c.getPwd().equals(pwd)) {
			throw new FindException("로그인 실패");
		}
	}
	
	public Customer detail(String id) throws FindException {
		return dao.selectById(id);
	}
	
	public void modify(Customer c) throws ModifyException {
		if(c.getEnabled() == 0) { //
			throw new ModifyException("탈퇴 작업은 할 수  없습니다");
		}
		dao.update(c);
	}
	
	public void leave(Customer c) throws ModifyException {
		c.setEnabled(0);
		dao.update(c);
	}
	
	public static void main(String[] args) {
		CustomerService service = CustomerService.getInstance();
//		Customer c = new Customer("id10", "id10", "id10");
//		try {
//			 service.signup(c);
//		} catch (AddException e) {
//			e.printStackTrace();
//		}
		
//		try {
//			 service.login("id8", "hi");
//		} catch (FindException e) {
//			e.printStackTrace();
//		}
		
		Customer c = new Customer("id10", "id10", "id10", null, 1);
		try {
			service.leave(c);
		} catch (ModifyException e) {
			e.printStackTrace();
		}
		
	}
}
