package com.bitacademy.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bitacademy.mysite.vo.BoardVo;

public class BoardDao {

	public boolean insertBoard(BoardVo vo) {
		boolean result = false;
		List<BoardVo> list = new ArrayList<BoardVo>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			conn = getConnection();

			sql = "insert into board(user_no, title, group_no, order_no, depth, contents, reg_date) "
					+ "	values(?, ?, ifnull((select max(group_no)+1 from board b),1), 1, 0, ?, now());";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, vo.getUserNo());
			pstmt.setString(2, vo.getTitle());
			pstmt.setString(3, vo.getContents());
			// 결과가 1이 아닌경우 false return
			result = 1 == pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	public boolean insertBoardReply(BoardVo originVo, BoardVo vo) {
		boolean result = false;
		List<BoardVo> list = new ArrayList<BoardVo>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			conn = getConnection();

			sql = "set sql_safe_updates=0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery();
			pstmt.close();

			sql = "update board " + "	set order_no = order_no + 1 " + " where group_no = ? and order_no >= ? + 1;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, originVo.getGroupNo());
			pstmt.setLong(2, originVo.getOrderNo());
			pstmt.executeUpdate();
			pstmt.close();

			sql = "insert into board(user_no, title, group_no, order_no, " + " depth, contents, reg_date) "
					+ "	values(?, ?, ?, ?+1, " + " ?+1, ?, now());";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, vo.getUserNo());
			pstmt.setString(2, vo.getTitle());
			pstmt.setLong(3, originVo.getGroupNo());
			pstmt.setLong(4, originVo.getOrderNo());
			pstmt.setLong(5, originVo.getDepth());
			pstmt.setString(6, vo.getContents());

			result = 1 == pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	
	public boolean replyInsert(BoardVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			String sql = "insert"	+ 
					"	into board (no, title, contents, user_no, count, reg_date, group_no, order_no, depth) "	+
					"   values (null, ?, ?,?, 0, now(), ?, ? , ?);";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3,  vo.getUserNo());
			pstmt.setLong(4, vo.getGroupNo());
			pstmt.setLong(5,  vo.getOrderNo());
			pstmt.setLong(6, vo.getDepth());
			int count = pstmt.executeUpdate();
			result = count == 1;

		} catch (SQLException e) {
			System.out.println("error-"+e);
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	
	public boolean updateOrderNo(Long groupNo, Long orderNo, Long no) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			conn = getConnection();
			sql = "update board set order_no= order_no+1 where group_no = ? and order_no > ? and no > 0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, groupNo);
			pstmt.setLong(2, orderNo);
			int count = pstmt.executeUpdate();
			result = count >= 1;

		} catch (SQLException e) {
			System.out.println("error-"+e);
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	
	public List<BoardVo> selectAll() {
		List<BoardVo> list = new ArrayList<BoardVo>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = getConnection();

			sql = "set sql_safe_updates=0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery();
			pstmt.close();

			sql = "select b.no, b.user_no, b.title, b.group_no, b.order_no, b.depth, "
					+ " date_format(b.reg_date,'%Y-%m-%d %H:%i:%s') as reg_date, cnt, u.name " + "	from board b "
					+ " join user u " + " on b.user_no = u.no " + "	order by group_no DESC, order_no ASC;";

			pstmt = conn.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardVo vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setUserNo(rs.getLong(2));
				vo.setTitle(rs.getString(3));
				vo.setGroupNo(rs.getLong(4));
				vo.setOrderNo(rs.getLong(5));
				vo.setDepth(rs.getLong(6));
				vo.setRegDate(rs.getString(7));
				vo.setCnt(rs.getLong(8));
				vo.setUserName(rs.getString(9));
				list.add(vo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return list;
	}

	public boolean deleteBoard(BoardVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			conn = getConnection();

			sql = " delete from board "
					+ "where no = ? and order_no = (select max(order_no) from (select order_no from board where group_no = ?) b) ;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, vo.getNo());
			pstmt.setLong(2, vo.getGroupNo());

			result = 1 == pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	public boolean updateBoardCnt(Long no) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		BoardVo vo = null;
		try {
			conn = getConnection();

			sql = "update board " + "	set cnt = cnt + 1" + " where no = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);

			result = 1 == pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	public BoardVo getBoard(Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		BoardVo vo = null;
		try {
			conn = getConnection();

			sql = " select b.no, b.user_no, b.title, b.group_no, b.order_no, "
					+ " b.depth, b.contents, date_format(b.reg_date,'%Y-%m-%d %H:%i:%s') as reg_date, cnt "
					+ "	from board b " + " join user u on b.no = ? and b.user_no = u.no;";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setUserNo(rs.getLong(2));
				vo.setTitle(rs.getString(3));
				vo.setGroupNo(rs.getLong(4));
				vo.setOrderNo(rs.getLong(5));
				vo.setDepth(rs.getLong(6));
				vo.setContents(rs.getString(7));
				vo.setRegDate(rs.getString(8));
				vo.setUserName(rs.getString(9));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return vo;
	}

	public boolean updateBoard(BoardVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = getConnection();
			// 조회수 증가
			sql = "update board " + "	set title = ?, contents = ? " + " where no = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getNo());
			// 결과가 1이 아닌경우 false return
			result = 1 == pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	public double totalCnt() {
		double res = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			String sql = "select count(*) from board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			rs.next();
			res = rs.getInt(1);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	/*
	public ArrayList<BoardVo> list(int start, int end) {
		ArrayList<BoardVo> res = new ArrayList<BoardVo>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			String sql = "select * from "
					+ 			"(select rownum rnum, t1.* from "
					+ 						"(select * from board order by group_no desc, order_no)"
					+ "			 t1) where rnum >= ? and rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardVo vo = new BoardVo();

				vo.setNo(rs.getLong(1));
				vo.setUserNo(rs.getLong(2));
				vo.setTitle(rs.getString(3));
				vo.setGroupNo(rs.getLong(4));
				vo.setOrderNo(rs.getLong(5));
				vo.setDepth(rs.getLong(6));
				vo.setContents(rs.getString(7));
				vo.setRegDate(rs.getString(8));
				vo.setUserName(rs.getString(9));

				res.add(vo);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		
		}

		return res;
	}

	*/
	
	public List<BoardVo> paging(int curpage, int shownum){
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet result = null;
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = getConnection();

			String sql = "select no, title, user_no, count, reg_date "+
					" from board order by no desc limit ?, ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (curpage - 1)*shownum);
			pstmt.setInt(2, shownum);
			result = pstmt.executeQuery();

			while(result.next()) {
				BoardVo vo = new BoardVo();
				vo.setNo(result.getLong(1));
				vo.setTitle(result.getString(2));
				vo.setUserNo(result.getLong(3));
				vo.setCnt(result.getLong(4));
				vo.setRegDate(result.getString(5));

				list.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("error-"+e);
		} finally {
			try {
				if(pstmt!=null)	pstmt.close();
				if(conn!=null) conn.close();
				if(result!=null) result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	// 제목으로 검색
	/*
	public ArrayList<BoardVo> search(String word) {
		ArrayList<BoardVo> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			System.out.println("검색한 단어:" + word);
			String sql = "select b.no, b.user_no, b.title, b.group_no, b.order_no, b.depth, date_format(b.reg_date,'%Y-%m-%d %H:%i:%s') as regdate, cnt, u.name"
					+ "from board b" + "join user u " + "on b.user_no = u.no" + "where title like ?"
					+ "order by group_no DESC, order_no ASC ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + word + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardVo vo = new BoardVo();

				vo.setNo(rs.getLong(1));
				vo.setUserNo(rs.getLong(2));
				vo.setTitle(rs.getString(3));
				vo.setGroupNo(rs.getLong(4));
				vo.setOrderNo(rs.getLong(5));
				vo.setDepth(rs.getLong(6));
				vo.setRegDate(rs.getString(7));
				vo.setCnt(rs.getLong(8));
				vo.setUserName(rs.getString(9));
				list.add(vo);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return list;
	}
	*/
	
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/webdb?characterEncoding=utf8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("error " + e);
		}
		return conn;
	}

}