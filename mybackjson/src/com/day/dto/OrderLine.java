package com.day.dto;

public class OrderLine {
	private int order_no;
	// private OrderInfo order_id; --주문 상세 테이블(line)에서 주문 기본 테이블 내용을 볼 일이 없기 때문에,
	// has-a관계를 사용하지 않는다.
	private Product order_p; // has-a 관계. 일반적으로 many table에서 one테이블을 가지고 있다는 의미. (주문 상세 테이블에서 상품 테이블의 내용을
								// 참조하는 일이 많다.)
	private int order_quantity;

	public OrderLine() {
		super();
	}

	public OrderLine(Product order_p, int order_quantity) {
		super();
		this.order_p = order_p;
		this.order_quantity = order_quantity;
	}

	public OrderLine(int order_no, Product order_p, int order_quantity) {
		super();
		this.order_no = order_no;
		this.order_p = order_p;
		this.order_quantity = order_quantity;
	}

	public int getOrder_no() {
		return order_no;
	}

	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}

	public Product getOrder_p() {
		return order_p;
	}

	public void setOrder_p(Product order_p) {
		this.order_p = order_p;
	}

	public int getOrder_quantity() {
		return order_quantity;
	}

	public void setOrder_quantity(int order_quantity) {
		this.order_quantity = order_quantity;
	}

	@Override
	public String toString() {
		return "OrderLine [order_no=" + order_no + ", order_p=" + order_p + ", order_quantity=" + order_quantity + "]";
	}

}
