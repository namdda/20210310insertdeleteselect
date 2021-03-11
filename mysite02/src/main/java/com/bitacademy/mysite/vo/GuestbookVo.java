package com.bitacademy.mysite.vo;

import java.util.Date;

public class GuestbookVo {

	
	private Long no;
	private String name;
	private String password;
	private String contents;
	//private Date regDate;
	
	private String regDate;
	
	public GuestbookVo() {
	
	}

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
/*
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
*/
	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	@Override
	public String toString() {
		return "GuestbookVo [no=" + no + ", name=" + name + ", password=" + password + ", contents=" + contents
				+ ", regDate=" + regDate + "]";
	}

	
	
	
	
	
}