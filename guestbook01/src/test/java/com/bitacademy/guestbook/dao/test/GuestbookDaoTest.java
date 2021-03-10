package com.bitacademy.guestbook.dao.test;

import java.util.List;


import com.bitacademy.guestbook.dao.GuestbookDao;
import com.bitacademy.guestbook.vo.GuestbookVo;

public class GuestbookDaoTest {
	public static void main(String[] args) {
		//insertTest
		//testInsert();
		
		//findAll Test
		testFindAll();
	}

	
	public static void testFindAll() {
		List<GuestbookVo> list= new GuestbookDao().findAll();
		
		for(GuestbookVo vo : list) {
			System.out.println(vo);
		}
	}
	
	public static void testInsert() {
		GuestbookVo vo = new GuestbookVo();
		vo.setName("수면부족");;
		vo.setPassword("sleeping");
		vo.setContents("잠이오는거 \n"+"실화인지");
		
		new GuestbookDao().insert(vo);
	}
}