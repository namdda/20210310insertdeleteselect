package com.bitacademy.mongodbsite.controller;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bitacademy.mongodbsite.dao.UserDao;
import com.bitacademy.mongodbsite.vo.UserVo;
import com.bitacademy.web.mvc.WebUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		
		if("joinform".equals(action)) {
			WebUtil.forward("/WEB-INF/views/user/joinform.jsp", request, response);
		}else if("loginform".equals(action)) {
			WebUtil.forward("/WEB-INF/views/user/loginform.jsp", request, response);
		}else if("updateform".equals(action)) {
			// Access control (접근 제어)
			HttpSession session = request.getSession();
			if(session == null){
				WebUtil.redirect(request.getContextPath(), request, response);
				return;
			}
			
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if(authUser == null) {
				WebUtil.redirect(request.getContextPath(), request, response);
				return;				
			}
			
			Long no = authUser.getNo();
			UserVo userVo = new UserDao().findByNo(no);
			
			request.setAttribute("userVo", userVo);			
			WebUtil.forward("/WEB-INF/views/user/updateform.jsp", request, response);
		}else if("logout".equals(action)) {
			HttpSession session = request.getSession();
			if(session == null) {
				WebUtil.redirect(request.getContextPath(), request, response);
				return;
			}
			// 로그 아웃 처리
			if(session != null && session.getAttribute("authUser")!= null) {
				session.removeAttribute("authUser");
				session.invalidate();
			}
			WebUtil.redirect(request.getContextPath(), request, response);			
		}else{
			WebUtil.redirect(request.getContextPath(), request, response);			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		if("login".equals(action)) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			UserVo userVo = new UserVo();
			userVo.setEmail(email);
			userVo.setPassword(password);
			
			UserVo authUser = new UserDao().findByEmailAndPassword(userVo); 
			System.out.println(authUser);
			if(authUser == null) {
				request.setAttribute("authResult", "fail");
				WebUtil.forward("/WEB-INF/views/user/loginform.jsp", request, response);
				return;
			}
			// 인증 처리 
			HttpSession session = request.getSession(true);
			session.setAttribute("authUser", authUser);
			
			// 응답
			WebUtil.redirect(request.getContextPath(), request, response);
			
//			response.setContentType("text/html; charset=UTF-8");
//			if(new UserDao().select(userVo)) {
////				PrintWriter writer = response.getWriter();
////				writer.println("<script>alert('로그인 성공');</script>");
////				writer.close();
//				WebUtil.redirect(request.getContextPath(), request, response);
//			}else {
//				PrintWriter writer = response.getWriter();
//				writer.println("<script>alert('로그인 실패');history.back();</script>");
//				writer.close();
//			}
		}else if("join".equals(action)) {
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			UserVo userVo = new UserVo();
			userVo.setName(name);
			userVo.setEmail(email);
			userVo.setPassword(password);
			userVo.setGender(gender);
			
			new UserDao().insert(userVo);
			WebUtil.redirect(request.getContextPath() + "/user?a=joinsuccess", request, response);
		}else if("update".equals(action)) {
			// Access control (접근 제어)
			HttpSession session = request.getSession();
			if(session == null){
				WebUtil.redirect(request.getContextPath(), request, response);
				return;
			}
			
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if(authUser == null) {
				WebUtil.redirect(request.getContextPath(), request, response);
				return;				
			}
			Long no = Long.parseLong(request.getParameter("no"));			
			String name = request.getParameter("name");			
			String email = request.getParameter("email");			
			String password = request.getParameter("password");		
			String gender = request.getParameter("gender");		
			
						
			UserVo userVo = new UserDao().findByNo(no);
			userVo.setName(name);
			userVo.setEmail(email);
			userVo.setPassword(password);
			userVo.setGender(gender);
			
			if(new UserDao().update(userVo)) {
				UserVo authEditedUser = new UserVo();
				authEditedUser.setNo(no);
				authEditedUser.setName(name);
				session.setAttribute("authUser",authEditedUser);
			}

			WebUtil.redirect(request.getContextPath(), request, response);			
		}
	}

}
