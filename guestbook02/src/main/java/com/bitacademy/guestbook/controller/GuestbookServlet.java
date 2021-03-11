package com.bitacademy.guestbook.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitacademy.guestbook.dao.GuestbookDao;
import com.bitacademy.guestbook.vo.GuestbookVo;
import com.bitacademy.web.mvc.WebUtil;

public class GuestbookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("b");
		
		if("deleteform".equals(action)) {
			WebUtil.forward("/WEB-INF/views/deleteform.jsp", request, response);
		}
		
		 else if("add".equals(action)) {
			 
				String name = request.getParameter("name");
				String password = request.getParameter("password");
				String contents = request.getParameter("contents");
				contents = contents.replaceAll("\n", "<br/>");
				
				GuestbookVo vo = new GuestbookVo();
				vo.setName(name);
				vo.setPassword(password);
				vo.setContents(contents);
				
				GuestbookDao dao = new GuestbookDao();
				dao.insert(vo);
				
				WebUtil.redirect(request.getContextPath() + "/gb", request, response);
			}
		 else if ("delete".equals(action)) {
				String no = request.getParameter("no");
				String password = request.getParameter("password");

				GuestbookVo vo = new GuestbookVo();
				vo.setNo(Long.parseLong(no));
				vo.setPassword(password);
				new GuestbookDao().delete(vo);

				WebUtil.redirect(request.getContextPath() + "/gb", request, response);
			} 
		else {
			List<GuestbookVo> list = new GuestbookDao().findAll();
			
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
