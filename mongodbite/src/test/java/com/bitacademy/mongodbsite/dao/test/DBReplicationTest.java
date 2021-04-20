package com.bitacademy.mongodbsite.dao.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBReplicationTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		select("master");
		System.out.println("success");
	}

	public static Connection getConnection(String dbName) throws SQLException {
		// connection map
		Map<String, Integer> portMap = new HashMap<>();
		portMap.put("master", 3307);
		portMap.put("slave1", 3308);
		portMap.put("slave2", 3309);
		portMap.put("slave3", 3310);
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:"+portMap.get(dbName)+"/webdb?characterEncoding=utf8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "masterpw");
		} catch (ClassNotFoundException e) {
			System.out.println("error " + e);
		}
		return conn;
	}

	public static void select(String dbName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = getConnection(dbName);

			sql = "select * from test;";

			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getInt(1));
			}
			System.out.println("done");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println("whatever");
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
	}

}
