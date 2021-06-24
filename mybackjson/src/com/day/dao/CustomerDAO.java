package com.day.dao;

import com.day.dto.Customer;
import com.day.exception.AddException;
import com.day.exception.FindException;
import com.day.exception.ModifyException;

/*
	INSERT INTO customer(id, pwd, name) VALUES('id1', 'p1', 'n1')
	SELECT * FROM customer WHERE id = 'id1'
	SELECT * FROM customer WHERE id = 'id1'
	UPDATE customer
	SET 	pwd = 'pp1', 
			name = 'nn1',
			buildingno = '1'
	WHERE id = 'id1'
	--DELETE customer WHERE id = 'id1'
	UPDATE customer
	SET enabled = 0
	WHERE id = 'id1'
*/

public interface CustomerDAO {
	/**
	 * 고객은 가입한다
	 * @param Customer 고객
	 * @return 
	 * @throws AddException 가입을 실패한 경우 발생한다
	 */
	public void insert(Customer c) throws AddException;
	
	/**
	 * 고객은 로그인하거나 자기 정보를 조회한다
	 * @param String id
	 * @return Customer 고객 정보
	 * @throws FindException 아이디 조회 실패 시 발생한다
	 */
	public Customer selectById(String id) throws FindException;
	
	/**
	 * 고객은 자기 정보를 수정하거나 탈퇴한다
	 * @param Customer 고객
	 * @return 
	 * @throws ModifyException 고객 정보 수정 실패 시 발생한다
	 */
	public void update(Customer c) throws ModifyException;
}
