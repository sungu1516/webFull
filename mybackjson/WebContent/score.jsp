<%@page import="java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>

<body>

	<%!int totalScore;
	int cnt;%>

	<%
		int scoreTemp = Integer.parseInt(request.getParameter("score"));
	totalScore += scoreTemp;
	cnt++;
	double avg = (double) totalScore / cnt;
	%>
	선택하신 별점은
	<%=scoreTemp%>
	점입니다.
	<br> 총 별점의 합은
	<%=totalScore%>
	점입니다.
	<br>
	<%
		String pattern = "0.0";
	DecimalFormat df = new DecimalFormat(pattern);
	%>
	<br> 총 별점의 평균은
	<%=df.format(avg)%>
	점입니다.
	<br>
	<hr>
	<a href="score.html">별점주기</a>
</body>

</html>