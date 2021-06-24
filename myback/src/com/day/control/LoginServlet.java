package com.day.control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.day.exception.FindException;
import com.day.service.CustomerService;

/**
 * Servlet implementation class LoginServlett
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//보안정책 허용
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		//1. 요청전달데이터 얻기
			
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
	
		ServletContext sc = getServletContext();
		CustomerService.envProp = sc.getRealPath(sc.getInitParameter("env"));
		CustomerService service = CustomerService.getInstance();
		//2. 비즈니스 로직을 호출
		String path = "";
		try {
			System.out.println("test : " + id + ":" + pwd);
			System.out.println(service);
			service.login(id, pwd);
			//3. 성공 시 
			path = "success.jsp";
			System.out.println("로그인 성공!");
			
		} catch (FindException e) {
			//e.printStackTrace();
			//4. 실패 시
			path = "fail.jsp";
			System.out.println("로그인 실패!");
		}
		
		//5. 페이지 이동
		RequestDispatcher rd = request.getRequestDispatcher(path);
		rd.forward(request, response);
	}

}
