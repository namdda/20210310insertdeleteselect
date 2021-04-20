package com.bitacademy.mongodbsite.controller;


import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitacademy.web.mvc.WebUtil;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet() called");
		int visitCount = 0;
//		getServletContext().setAttribute(getServletName(), response);
		
		// 쿠키 읽기
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if("visitCount".equals(cookie.getName())) {
					visitCount = Integer.parseInt(cookie.getValue());
				}
			}
		}
		// 쿠키 쓰기 
		visitCount++;
		Cookie cookie = new Cookie("visitCount", String.valueOf(visitCount));
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(24* 60 * 60); //day
		response.addCookie(cookie);
		
		// 로그 페이지
		String action = request.getParameter("a");
		if("log".equals(action)){
			WebUtil.forward("/WEB-INF/views/main/log.jsp", request, response);
		}else {
			WebUtil.forward("/WEB-INF/views/main/index.jsp", request, response);			
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//System.out.println("service() called");
		// TODO Auto-generated method stub
		super.service(req, resp);
	}
	@Override
	public void destroy() {
		//System.out.println("destroy() called");
		// TODO Auto-generated method stub
		super.destroy();
	}
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		//System.out.println("init() called");
		//String configPath = this.getServletConfig().getInitParameter("config");
		//System.out.println("config path - " + configPath);
		super.init();
		
	}

}
