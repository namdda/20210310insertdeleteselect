package com.bitacademy.mysite.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bitacademy.mysite.dao.MBoardDao;
import com.bitacademy.mysite.vo.MBoardVo;
import com.bitacademy.mysite.vo.MPageVo;
import com.bitacademy.mysite.vo.UserVo;
import com.bitacademy.web.mvc.WebUtil;

public class MBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		String action = request.getParameter("a");
		UserVo userVo = null;

		if ("writeform".equals(action)) {
			
			WebUtil.forward("/WEB-INF/views/Mboard/write.jsp", request, response);
		} else if ("write".equals(action)) {
			HttpSession session = request.getSession();
			if (session != null && session.getAttribute("authUser") != null) {
				userVo = (UserVo) request.getSession(false).getAttribute("authUser");
			} else {
			
			
				WebUtil.forward("/WEB-INF/views/user/loginform.jsp", request, response);
				return;
			}

			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String name = userVo.getName();
			Long userno = userVo.getNo();

			MBoardVo boardVo = new MBoardVo();
			boardVo.setTitle(title);
			boardVo.setContent(content);
			boardVo.setName(name);
			boardVo.setUserNo(userno);

			if (!request.getParameter("group_no").equals("")) {
				boardVo.setGroup_no(Integer.parseInt(request.getParameter("group_no")));
			}
			if (!request.getParameter("order_no").equals("")) {
				boardVo.setOrder_no(Integer.parseInt(request.getParameter("order_no")));
			}
			if (!request.getParameter("depth").equals("")) {
				boardVo.setDepth(Integer.parseInt(request.getParameter("depth")));
			}

			String reply = "N";
			if (!request.getParameter("reply").equals("")) {
				reply = request.getParameter("reply");
			}

			MBoardDao.getInstance().insert(boardVo, reply);
			
			WebUtil.redirect(request.getContextPath() + "/Mboard?a=index", request, response);

		} else if ("view".equals(action)) {
			Long no = Long.parseLong(request.getParameter("no"));

			MBoardVo boardVo = MBoardDao.getInstance().findOne(no);
			System.out.println(boardVo);
			request.setAttribute("boardVo", boardVo);
			
			WebUtil.forward("/WEB-INF/views/Mboard/view.jsp", request, response);
		} else if ("updateform".equals(action)) {
			Long no = Long.parseLong(request.getParameter("no"));

			MBoardVo boardVo = MBoardDao.getInstance().findOne(no);
			request.setAttribute("boardVo", boardVo);
			
			WebUtil.forward("/WEB-INF/views/Mboard/modify.jsp", request, response);
		} else if ("update".equals(action)) {
			Long no = Long.parseLong(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			MBoardVo boardVo = new MBoardVo();
			boardVo.setNo(no);
			boardVo.setTitle(title);
			boardVo.setContent(content);
			MBoardDao.getInstance().update(boardVo);

			WebUtil.redirect(request.getContextPath() + "/Mboard", request, response);
		} else if ("delete".equals(action)) {
			System.out.println(request.getParameter("no"));
			Long no = Long.parseLong(request.getParameter("no"));
			MBoardDao.getInstance().delete(no);
		
			WebUtil.redirect(request.getContextPath() + "/Mboard", request, response);
		} else {
			int nowPage = 1;
			String search = "";

			if (request.getParameter("p") != null && !("".equals(request.getParameter("p")))) {
				nowPage = Integer.parseInt(request.getParameter("p"));
			}

			int totalCount = 0;
			List<MBoardVo> list = null;
			MPageVo PageVo = null;

			if (request.getParameter("search") != null && !("".equals(request.getParameter("search")))) {
				totalCount = MBoardDao.getInstance().searchTotalCount(search);
				PageVo = new MPageVo(totalCount, 5, nowPage);
				list = MBoardDao.getInstance().search(search, PageVo);
			} else {
				totalCount = MBoardDao.getInstance().getTotalCount();
				PageVo = new MPageVo(totalCount, 5, nowPage);
				list = MBoardDao.getInstance().findAll(PageVo);
			}

			request.setAttribute("list", list);
			request.setAttribute("PageVo", PageVo);
			request.setAttribute("search", search);

			WebUtil.forward("WEB-INF/views/Mboard/index.jsp", request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
