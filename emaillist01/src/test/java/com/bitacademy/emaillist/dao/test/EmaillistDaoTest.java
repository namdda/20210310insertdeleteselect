package com.bitacademy.emaillist.dao.test;

import java.util.List;

import com.bitacademy.emaillist.dao.EmaillistDao;
import com.bitacademy.emaillist.vo.EmaillistVo;

public class EmaillistDaoTest {
	
	public static void main(String[] args) {
		// findAll test
		testFindAll();
	}
		
	public static void testFindAll() {
		List<EmaillistVo> list = new EmaillistDao().findAll();
		for(EmaillistVo vo  : list) {
			System.out.println(vo);
		}
	}
}
