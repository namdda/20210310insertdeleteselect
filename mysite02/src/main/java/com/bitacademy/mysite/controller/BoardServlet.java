package com.bitacademy.mysite.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitacademy.mysite.dao.BoardDao;
import com.bitacademy.mysite.vo.BoardVo;
import com.bitacademy.web.mvc.WebUtil;

public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("a");
		
		
				if("writeform".equals(action)) {
					String boardNo = request.getParameter("b"); 
					if(boardNo != null) {
						request.setAttribute("boardNo", Long.parseLong(boardNo));
					}
					WebUtil.forward("/WEB-INF/views/board/write.jsp", request, response);
				
				}else if("write".equals(action)) {
					Long userNo = Long.parseLong(request.getParameter("user_no"));
					String title = request.getParameter("title");
					String content = request.getParameter("content");
					BoardVo vo = new BoardVo();
					vo.setUserNo(userNo);
					vo.setTitle(title);
					vo.setContents(content);

					new BoardDao().insertBoard(vo);
					WebUtil.redirect(request.getContextPath() + "/board", request, response);
				
				}else if("reply".equals(action)) {
					Long boardNo = Long.parseLong(request.getParameter("bno"));
					Long userNo = Long.parseLong(request.getParameter("user_no"));
					String title = request.getParameter("title");
					String content = request.getParameter("content");
					BoardVo vo = new BoardVo();
					vo.setUserNo(userNo);
					vo.setTitle(title);
					vo.setContents(content);
					BoardVo originVo = new BoardDao().getBoard(boardNo);
					new BoardDao().insertBoardReply(originVo,vo);
					WebUtil.redirect(request.getContextPath() + "/board", request, response);
				
				}else if("modifyform".equals(action)) {
					Long no = Long.parseLong(request.getParameter("no"));
					new BoardDao().updateBoardCnt(no);
					BoardVo vo = new BoardDao().getBoard(no);
					request.setAttribute("vo", vo);
					WebUtil.forward("/WEB-INF/views/board/modify.jsp", request, response);
				
				}else if("modify".equals(action)) {
					Long no = Long.parseLong(request.getParameter("no"));
					String title = request.getParameter("title");
					String content = request.getParameter("content");
					BoardVo vo = new BoardVo();
					vo.setNo(no);
					vo.setTitle(title);
					vo.setContents(content);
					new BoardDao().updateBoard(vo);
					WebUtil.redirect(request.getContextPath() + "/board?a=view&no="+no, request, response);
				
				}else if("view".equals(action)) {
					Long no = Long.parseLong(request.getParameter("no"));
					new BoardDao().updateBoardCnt(no);
					BoardVo vo = new BoardDao().getBoard(no);
					request.setAttribute("vo", vo);
					WebUtil.forward("/WEB-INF/views/board/view.jsp", request, response);	
				
					
				}else if("delete".equals(action)) {	
					Long no = Long.parseLong(request.getParameter("no"));
					BoardVo vo = new BoardDao().getBoard(no);
					boolean result = new BoardDao().deleteBoard(vo);
					if(result) {
						WebUtil.redirect(request.getContextPath() + "/board", request, response);							
						return;
					}else {
						// 답글있는 글 삭제 시도 시 
						response.setCharacterEncoding("UTF-8"); 
						response.setContentType("text/html; charset=UTF-8");
						PrintWriter out = response.getWriter();
						out.println("<script>alert('답글이 있는 글은 지울 수 없습니다.');location.href='" + request.getContextPath() + "/board';</script>");
						out.flush();
					}
				}else {
					
					//내 멋대로 페이징 
					
					int nowPage = 1;
					int pageLimit = 5;
					int pageNumLimit = 5;
					/*
					if(request.getParameter("nowPage")!=null)
						nowPage = Integer.parseInt(request.getParameter("nowPage"));
					
					BoardDao dao = new BoardDao();
					
					List<BoardVo> list = new BoardDao().selectAll();
					
					int lastPage = (int)Math.ceil(dao.totalCnt()/pageLimit);
					
					int start = (nowPage-1)*pageLimit+1;
					int end = nowPage*pageLimit;
					
					int startPage = (nowPage-1)/pageNumLimit*pageNumLimit+1;
					int endPage = startPage+pageNumLimit-1;
					
					if(endPage>lastPage)
						endPage=lastPage;
					
					request.setAttribute("startPage", startPage);
					request.setAttribute("endPage", endPage);
					request.setAttribute("start", start);
					request.setAttribute("nowPage", nowPage);
					request.setAttribute("lastPage", lastPage);
					request.setAttribute("data", dao.list(start, end));
				
					*/
					List<BoardVo> list = new BoardDao().selectAll();
					request.setAttribute("list", list);
					WebUtil.forward("/WEB-INF/views/board/index.jsp", request, response);		
				}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}