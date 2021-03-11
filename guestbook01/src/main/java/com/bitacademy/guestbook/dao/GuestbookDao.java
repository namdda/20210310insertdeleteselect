package com.bitacademy.guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bitacademy.guestbook.vo.GuestbookVo;


public class GuestbookDao {
	
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		
		try {
			//1. JDBC Driver 로딩
			Class.forName("com.mysql.cj.jdbc.Driver");
		
			//2. 연결하기
			String url = "jdbc:mysql://localhost:3306/webdb?characterEncoding=utf8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

		} catch (ClassNotFoundException e) {
			System.out.println("error-" + e);
		}

		return conn;
	}
	
	public List<GuestbookVo> findAll(){
		List<GuestbookVo> list = new ArrayList < >();
		
		Connection conn = null;
		PreparedStatement pstmt = null; //statement를 상속받는 인터페이스로 SQL구문을 실행시키는 기능을 갖는 객체
		ResultSet rs = null; //ResultSet : 명령에 대한 반환값.

		
		try {
			conn = getConnection();
			
			//3. SQL 준비
			// date_format을 사용하고 싶다면 vo의 Regdate 타입을 처음부터 String 으로 받으면 문제가 없다. 
			// 기본 Date를 사용하고 싶다면 1. 우선 import 은 util.date가 아닌 sql.date로 할 것. 
			// 원래는 이걸 사용 해야 하는걸로 알고 있는데, 우선은 이걸로 하기로 했다. (https://yuja-kong.tistory.com/26)
			String sql = "select no, name, date_format(reg_date, '%Y-%m-%d  %H: %i:%s') as reg_date, contents" +
					" from guestbook" +
					" order by no desc";
			pstmt = conn.prepareStatement(sql);
			
			//4. 바인딩 --> 기억저장소에 할당하는 코드 (얘는 executeupdate 하는 것이 없기에 딱히 바인딩 할 것이 없다, 있는거 뽑아내는거니 )
			
			//5. SQL문 실행
			rs = pstmt.executeQuery(); //DB에 명령

			//6. 데이터 가져오기
			while(rs.next()) {
				Long no = rs.getLong(1);
				String Name = rs.getString(2);
				// regDate = rs.getDate(3);
				String regDate =rs.getString(3);
				String contents = rs.getString(4);
				
				
				GuestbookVo vo = new GuestbookVo();
				vo.setNo(no);
				vo.setName(Name);
				vo.setRegDate(regDate);
				vo.setContents(contents);
				
				
				list.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);	
		} finally {
			// 자원정리
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		
		return list;
	}
	
	
	public boolean insert(GuestbookVo vo) {
		boolean result = false;
		
		Connection conn = null; 
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			//3. SQL 준비
			// sysdate와 now의 차이. sysdate는 말 그대로 지금 시스템 시간을 기록하는거라 업데이트 할 때마다 유동적으로 변할 수 있다고 한다. (insert보단 update에 맞다 판단)
			// now는 해당 쿼리문이 실행되었을 때의 시간이 땅땅 기록되는거라 안 바뀐다고 한다. 
			/*
			 * 쿼리( 쿼리 단위 )가 실행 될 때를 기준으로 날짜와 시간을 맞추고 싶다면 NOW를 사용하고,
			 중간중간 날짜를 조회할 때를 기준으로 각각 날짜와 시간을 사용하고 싶다면 SYSDATE를 사용합니다.
			* */
			String sql = "insert into guestbook values(null, ?, ?, ?, now())";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getPassword());
				pstmt.setString(3, vo.getContents());
				
				//4. 실행 
				int count = pstmt.executeUpdate(); // int executeUpdate() throws SQLException;
				
				//execute type = booleam
					//리턴값이 ResultSet 일 경우에는 true, 이 외의 경우에는 false 로 출력됩니다.
					//리턴값이 ResultSet 이라고 하여 ResultSet 객체에 결과값을 담을 수 없습니다.
				
				//executeQuery type = ResultSet
					//수행결과로 ResultSet 객체의 값을 반환합니다.
				
				//executeUpdate type = int  
					// INSERT / DELETE / UPDATE 관련 구문에서는 반영된 레코드의 건수를 반환합니다.
					//CREATE / DROP 관련 구문에서는 -1 을 반환합니다
				
				
				// 5. 결과
				result = count == 1; //count 가 1이면 true 인걸 사용한다. 
		}catch (SQLException e) {
			System.out.println("error:" + e);	
		} finally {
			// 자원정리
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		
		return result;
	}
	

	
	public boolean delete(String no, String password) {
		boolean result = false;

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();

			// 3. sql문 준비
			//no가 필요한 이유는 지우는 글이 어떤 것인지 프로그램이 알 수 있는 방법이 없기 때문. 
			//no가 ? 인 글을 지워주세요 하는게 본질이다. 그리고 이 값은 사용자가 입력해서 실행하는게 이상하기 때문에, 
			//받아와야 한다. (즉, 자바 코드가 아닌 표현식) 
			String sql = "delete from guestbook where no = ? and password = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, no);
			pstmt.setString(2, password);

			// 4. 실행
			int count = pstmt.executeUpdate();

			// 5. 결과
			result = count == 1;

		} catch (SQLException e) {
			// 1. 사과
			// 2. log
			System.out.println("error: " + e);
		} finally {
			try {
				// 자원 정리
				//여기에 있는 if문도 반복되니 getConnection() 처럼 메소드로 빼는 것도 나쁘지 않을 것 같다.
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

}