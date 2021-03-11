package com.bitacademy.emaillist.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitacademy.emaillist.dao.EmaillistDao;
import com.bitacademy.emaillist.vo.EmaillistVo;
import com.bitacademy.web.mvc.WebUtil;


public class EmaillistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		
//		String uri = request.getRequestURI();
//		String[] paths = uri.split("/");
//		action = paths[paths.length-1];
		
		if("form".equals(action)) {
			WebUtil.forward("/WEB-INF/views/form.jsp", request, response);
		} else if("add".equals(action)) {
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");

			EmaillistVo vo = new EmaillistVo();
			vo.setFirstName(firstName);
			vo.setLastName(lastName);
			vo.setEmail(email);
			
			new EmaillistDao().insert(vo);
			
			WebUtil.redirect(request.getContextPath() + "/el", request, response);
		}else {
			List<EmaillistVo> list = new EmaillistDao().findAll();
			
			// forwarding = request dispatch = request extension
			request.setAttribute("list", list);
			WebUtil.forward("/WEB-INF/views/index.jsp", request, response);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
