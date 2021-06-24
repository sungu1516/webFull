<%@page import="com.day.dto.OrderInfo"%>
<%@page import="com.day.dto.Customer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% //이 부분은 사실상 servlet에서 할 일
request.setAttribute("resultInt", 1);
request.setAttribute("resultCustomer", new Customer("id1", "p1", "n1"));
%>

<% //이동된 jsp에서 할 일
//요청객체의 속성(이름:"resultInt")값 얻기
//int resultInvalue = (Integer)request.getAttribute("resultInt");
int resultIntValue = 0;
Integer resultIntegerValue = (Integer)request.getAttribute("resultInt");
if(resultIntegerValue != null){
	resultIntValue = resultIntegerValue.intValue();
}
%>

<%-- <%
//요청객체의 속성(이름:"resultCustomer")값 얻기
Customer resultC = (Customer) request.getAttribute("resultCustomer");
//요청객체가 null이면 매개변수없는생성자를 이용해 객체생성하기, 
//요청객체속성(이름:"resultCustomer")으로 추가하기
if(resultC == null) {
	resultC = new Customer();
	request.setAttribute("resultCustomer", resultC);
}
%> --%>
<jsp:useBean id="resultCustomer" class = "com.day.dto.Customer" scope = "request"/>

<%-- <%
resultC.setEnabled(0);
%> --%>

<jsp:setProperty name = "resultCustomer" property="enabled" value = "0"/>

<%-- out.print(resultC.getId()); --%>
<jsp.getProperty name = "resultCustomer" property="enabled"/>

<%--
//요청속성(이름:"orderInfo")얻기
//null인 경우 객체생성 후 요청속성으로 추가
OrerInfo oi = request.getAttribute("orderInfo");
if(oi==null) {
	oi = new OrderInfo();
	request.setAttribute("orderInfo", oi);
}
--%>
<%-- <jsp:useBean id = "orderOnfo" class = "com.day.dto.OrderInfo" scope="request"/> --%>
<%--//요청속성(이름:"orderInfo")의 프로퍼티 중 order_c값을 설정한다.
Customer c = new Customer();
oi.setOrder_c(c);
--%>


<h3>EL표기법에 의한 id값 : ${requestScope.orderInfo.order_c.id}</h3>
<%-- <h3>JSP expression에 의한 id값 : <%=((OrderInfo)request.getAttribute("orderInfo")).getOrder_c().getId() %></h3> --%>
<h3>EL표기법에 의한 요청전달데이터 word값 : ${param.word}</h3><%-- 요청전달데이터 없는경우 결과:"", 요청전달데이터이름있고 값없는경우ex)?word= 결과 : "" --%>
<h3>JSP expression에 의한 요청전달데이터 word값 : <%=request.getParameter("word") %></h3><%-- 요청전달데이터 없는경우 결과: null, 요청전달데이터이름있고 값없는경우ex)?word= 결과 : "" --%>
