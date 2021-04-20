package com.bitacademy.mongodbsite.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitacademy.mongodbsite.dao.BoardDao;
import com.bitacademy.mongodbsite.pagination.PagingBean;
import com.bitacademy.mongodbsite.vo.BoardVo;
import com.bitacademy.web.mvc.WebUtil;

/**
 * Servlet implementation class BoardServlet
 */
@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("a");
		
		// 글쓰기 페이지 
		if("writeform".equals(action)) {
			String boardNo = request.getParameter("b"); 
			if(boardNo != null) {
				request.setAttribute("boardNo", Long.parseLong(boardNo));
			}
			WebUtil.forward("/WEB-INF/views/board/write.jsp", request, response);
		// 게시글 등록 기능
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
		// 답글 등록 기능
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
		// 글 수정 페이지 
		}else if("modifyform".equals(action)) {
			Long no = Long.parseLong(request.getParameter("no"));
			new BoardDao().updateBoardViews(no);
			BoardVo vo = new BoardDao().getBoard(no);
			request.setAttribute("vo", vo);
			WebUtil.forward("/WEB-INF/views/board/modify.jsp", request, response);
		// 글 수정 기능
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
		// 게시글 보기 페이지
		}else if("view".equals(action)) {
			Long no = Long.parseLong(request.getParameter("no"));
			new BoardDao().updateBoardViews(no);
			BoardVo vo = new BoardDao().getBoard(no);
			request.setAttribute("vo", vo);
			WebUtil.forward("/WEB-INF/views/board/view.jsp", request, response);	
		// 게시글 삭제 기능
		// 답글이 없는 글만 삭제할 수 있도록 한다. 
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
			// 검색어가 존재하면 utf-8으로 decoding 한 후 검색 결과를 보여준다.
			String keyword = request.getParameter("kwd");
			String category = request.getParameter("category");
			int totalCount;
			List<BoardVo> list;
			
			if(keyword == null) {
				totalCount = new BoardDao().selectBoardListCnt();			
			}else{
				keyword = URLDecoder.decode(keyword,"UTF-8");
				totalCount = new BoardDao().selectBoardListCnt(category,keyword);
			}
			//TODO : paging 이 1페이지에서 번호 3 ~ 7 로 고정되어 나온다.
			String pageNo = request.getParameter("page");
			PagingBean pagingBean = null;			
			if(pageNo == null) {
				pagingBean = new PagingBean(totalCount);
			}else {
				pagingBean = new PagingBean(totalCount, Integer.parseInt(pageNo)); 
			}
			// 검색어 유무에 관련없이 paging 처리해서 목록을 출력한다. 
			if(keyword== null) {
				list = new BoardDao().getBoardPageList(pagingBean);				
			}else {
				list = new BoardDao().searchBoardListByKeyword(pagingBean,category,keyword);
				request.setAttribute("keyword", keyword);
				request.setAttribute("category", category);
			}
			
			request.setAttribute("list", list);
			request.setAttribute("pagingBean", pagingBean);
			
			WebUtil.forward("/WEB-INF/views/board/index.jsp", request, response);			
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}