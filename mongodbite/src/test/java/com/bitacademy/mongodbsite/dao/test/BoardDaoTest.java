package com.bitacademy.mongodbsite.dao.test;

import java.util.List;

import com.bitacademy.mongodbsite.dao.BoardDao;
import com.bitacademy.mongodbsite.pagination.PagingBean;
import com.bitacademy.mongodbsite.vo.BoardVo;

public class BoardDaoTest {
	public static void main(String[] args) {
		// 게시물 삽입 테스트
		for(int i=0 ; i< 100 ;i++) {
			insertBoardTest(i);			
		}
//		
		// 답글 삽입 테스트
//		insertBoardReplyTest();
		
		// 조회수 증가 테스트
//		viewBoardTest();
		
		// 게시물 삭제 테스트 
//		deleteBoardTest();
		
		// 게시물 수정 테스트 
//		updateBoardTest();
		
		
		
		// 게시물 전체 조회 테스트
		selectAllTest();
		
		// 제목 검색 조회 테스트
//		searchBoardListByTitleTest("user","jinsa");
		
//		selectVariableTest();

	}
	public static void searchBoardListByTitleTest(String column, String keyword) {
		int totalCount = new BoardDao().selectBoardListCnt(column,keyword);
		System.out.println(totalCount);
		String pageNo = null;
		PagingBean pagingBean = null;
		if(pageNo == null) {
			pagingBean = new PagingBean(totalCount);
		}
		List<BoardVo> list = new BoardDao().searchBoardListByKeyword(pagingBean,column,keyword);
		for(BoardVo vo : list) {
			System.out.println(vo);
		}
	}
	
	public static void selectVariableTest() {
//		new BoardDao().setSqlSafeUpdates(false);
//		new BoardDao().selectVariable(); 
//		new BoardDao().setSqlSafeUpdates(true);
//		new BoardDao().selectVariable(); 
	}
	public static void insertBoardTest(int no) {
		BoardVo vo = new BoardVo();
		Long userNo = 2L;
		String title = "테스트 타이틀 " + no;
		String contents = "test contents " + no;
		vo.setUserNo(userNo);
		vo.setTitle(title);
		vo.setContents(contents);
		new BoardDao().insertBoard(vo);
	}
	
	public static void selectAllTest() {
		int totalCount = new BoardDao().selectBoardListCnt();
		String pageNo = null;
		PagingBean pagingBean = null;			
		if(pageNo == null) {
			pagingBean = new PagingBean(totalCount);
		}
		List<BoardVo> list = new BoardDao().getBoardPageList(pagingBean);
		for(BoardVo vo : list) {
			System.out.println(vo);
		}		
	}
	public static void insertBoardReplyTest() {
		BoardVo ori = new BoardDao().getBoard(6L); 
		BoardVo vo = new BoardVo(); 
		Long userNo = 9L;
		String title = "노가다판도 피튀긴다";
		String contents = "ㄴㄴㄴ";
		vo.setUserNo(userNo);
		vo.setTitle(title);
		vo.setContents(contents);
		new BoardDao().insertBoardReply(ori, vo);		
	}
	public static void viewBoardTest() {
		new BoardDao().getBoard(2L);
	}
	public static void deleteBoardTest() {
		BoardVo vo = new BoardDao().getBoard(2L);
		new BoardDao().deleteBoard(vo);
	}
	public static void updateBoardTest() {
		BoardVo vo = new BoardVo(); 
		Long no = 3L;
		String title = "한강 수온체크 ㄱㄱㄱ";
		String contents = "ㄷㄷㄷ";
		vo.setNo(no);
		vo.setTitle(title);
		vo.setContents(contents);
		new BoardDao().updateBoard(vo);
	}
	

}
