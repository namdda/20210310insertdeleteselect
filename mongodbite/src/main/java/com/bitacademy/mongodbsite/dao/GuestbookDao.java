package com.bitacademy.mongodbsite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.bitacademy.mongodbsite.vo.GuestbookVo;
import com.bitacademy.mongodbsite.vo.UserVo;
import com.bitacademy.web.mvc.WebUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

/*
 * 데이터에 대한 접근, 조작을 제어하는 Controller ( Data Access Object)
 * 
 */
public class GuestbookDao {
	/*
	 * Client, DB, Collection을 호출하는 method
	 */
	public static MongoClient getClient(String host, int port) throws Exception {
		return MongoClients.create("mongodb://" + host + ":" + port);
	}

	public static MongoClient getClient() throws Exception {
		return getClient("localhost", 27017);
	}

	public static MongoDatabase getDB(MongoClient client, String databaseName) throws Exception {
		return client.getDatabase(databaseName);
	}

	public static MongoDatabase getDB(MongoClient client) throws Exception {
		return getDB(client, "nam");
	}

	public static MongoCollection<Document> getCollection(MongoDatabase database, String collectionName)
			throws Exception {
		return database.getCollection(collectionName);
	}

	public static MongoCollection<Document> getCollection(MongoDatabase database) throws Exception {
		return database.getCollection("guestbook");
	}

	// auto increment를 대신하는 counter collection
	private static int updateCounter(MongoDatabase database) {
		MongoCollection<Document> collection = database.getCollection("gbcounter");
		Document docRevised = collection.findOneAndUpdate(Filters.eq("_id", "userid"), Updates.inc("seq", 1));
		return (int)docRevised.get("seq");
	}


	// 방명록 추가
	public boolean insert(GuestbookVo vo) {
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);

			int no = updateCounter(db);
			Document doc = new Document().append("no", no).append("name", vo.getName())
					.append("password", vo.getPassword()).append("contents", vo.getContents())
					.append("reg_date", new Date());
			collection.insertOne(doc);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}

	// 전체 방명록 조회, 날짜는 sql에서 String format 반환됨
	public List<GuestbookVo> selectAll() {
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();

		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		Document resultDoc = null;
		GuestbookVo guestbookVo = null;
		MongoCursor<Document> cursor = null;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			cursor = collection.find().iterator();
			try {
				while (cursor.hasNext()) {
					resultDoc = cursor.next();
					guestbookVo = new GuestbookVo();
//					System.out.println(resultDoc.get("no").getClass().getName());
					int no = (int)resultDoc.get("no"); 
					guestbookVo.setNo(no);
					guestbookVo.setName((String) resultDoc.get("name"));
					guestbookVo.setPassword((String) resultDoc.get("password"));
					guestbookVo.setContents((String) resultDoc.get("contents"));
					guestbookVo.setRegDate(WebUtil.getFormatDate((Date) resultDoc.get("reg_date")));
					list.add(guestbookVo);
				}
			} finally {
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return list;
	}

	// 번호와 비밀번호를 통한 방명록 메시지 삭제
	public boolean delete(GuestbookVo vo) {
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);

			collection.deleteOne(Filters.and(
					Filters.eq("no", vo.getNo())
					,Filters.eq("password",vo.getPassword())));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}
}
