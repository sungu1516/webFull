package com.day.dto;

import java.util.Date;
import java.util.List;

public class OrderInfo {
	private String order_no;
	private Customer order_c; // 다의 입장에서 일을 has-a 관계로 설정했다. 이는, many인 orderInfo가 Customer을 갖고 있음을 의미한다.
	private java.util.Date order_dt;
	private List<OrderLine> lines;

	public OrderInfo() {
		super();
	}

	public OrderInfo(Customer order_c) {
		super();
		this.order_c = order_c;
	}

	public OrderInfo(Customer order_c, List<OrderLine> lines) {
		super();
		this.order_c = order_c;
		this.lines = lines;
	}

	public OrderInfo(String order_no, Customer order_c, Date order_dt, List<OrderLine> lines) {
		super();
		this.order_no = order_no;
		this.order_c = order_c;
		this.order_dt = order_dt;
		this.lines = lines;
	}

	// geeter & setter

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public Customer getOrder_c() {
		return order_c;
	}

	public void setOrder_c(Customer order_c) {
		this.order_c = order_c;
	}

	public java.util.Date getOrder_dt() {
		return order_dt;
	}

	public void setOrder_dt(java.util.Date order_dt) {
		this.order_dt = order_dt;
	}

	public List<OrderLine> getLines() {
		return lines;
	}

	public void setLines(List<OrderLine> lines) {
		this.lines = lines;
	}

	@Override
	public String toString() {
		return "OrderInfo [order_no=" + order_no + ", order_c=" + order_c + ", order_dt=" + order_dt + ", lines="
				+ lines + "]";
	}

}
