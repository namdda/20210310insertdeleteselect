package com.bitacademy.helloweb.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JoinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// POST 방식으로 넘어오는 데이터의 엔코딩
		request.setCharacterEncoding("UTF-8");
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String birthYear = request.getParameter("birthYear");
		String gender = request.getParameter("gender");
		String[] hobbies = request.getParameterValues("hobbies");
		String desc = request.getParameter("desc");
		
		System.out.println(email);
		System.out.println(password);
		System.out.println(birthYear);
		System.out.println(gender);
		if( hobbies != null ) {
			for(String hobby : hobbies) {
				System.out.println(hobby);
			}
		}
		System.out.println(desc);
		
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print("OK");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
