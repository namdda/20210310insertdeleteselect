package com.bitacademy.mongodbsite.dao;

import java.util.Date;

import org.bson.Document;

import com.bitacademy.web.mvc.WebUtil;
import com.bitacademy.mongodbsite.vo.UserVo;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;


public class UserDao {
	/*
	 *  Client, DB, Collection을 호출하는 method
	 */
	public static MongoClient getClient(String host,int port) throws Exception{
		return MongoClients.create("mongodb://"+host+":"+port);			
	}
	public static MongoClient getClient() throws Exception{
		return getClient("localhost",27017);
	}
	public static MongoDatabase getDB(MongoClient client,String databaseName) throws Exception{
		return client.getDatabase(databaseName);
	}
	public static MongoDatabase getDB(MongoClient client) throws Exception{
		return getDB(client,"nam");
	}
	public static MongoCollection<Document> getCollection(MongoDatabase database,String collectionName) throws Exception{
		return database.getCollection(collectionName);
	}
	public static MongoCollection<Document> getCollection(MongoDatabase database) throws Exception{
		return database.getCollection("user");
	}
	
	// auto increment를 대신하는 counter collection 
	private static Long updateCounter(MongoDatabase database) {
		MongoCollection<Document> collection = database.getCollection("counter");
		Document docRevised = collection.findOneAndUpdate(Filters.eq("_id","userid"),Updates.inc("seq",1));
		return Long.valueOf(docRevised.get("seq").toString());
	}
	// 이메일, 비밀번호가 일치하는 user를 가져온다. 
	public UserVo findByEmailAndPassword(UserVo vo) {
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		Document resultDoc = null;
		UserVo userVo = null;		
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			resultDoc = collection.find(Filters.and(
					Filters.eq("email", vo.getEmail())
					,Filters.eq("password", vo.getPassword())))
					.first();
			if(resultDoc != null) {
				userVo = new UserVo();
				userVo.setNo((Long)resultDoc.get("no"));
				userVo.setName((String)resultDoc.get("name"));
				userVo.setEmail((String)resultDoc.get("email"));
				userVo.setPassword((String)resultDoc.get("password"));
				userVo.setGender((String)resultDoc.get("gender"));
				userVo.setJoinDate(WebUtil.getFormatDate((Date)resultDoc.get("join_date")));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			client.close();
		}
		return userVo;
	}
	// no가 일치하는 user를 가져온다. 
	public UserVo findByNo(Long no) {
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		Document resultDoc = null;
		UserVo userVo = null;		
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			resultDoc = collection.find(Filters.eq("no", no)).first();
			if(resultDoc != null) {
				userVo = new UserVo();
				userVo.setNo((Long)resultDoc.get("no"));
				userVo.setName((String)resultDoc.get("name"));
				userVo.setEmail((String)resultDoc.get("email"));
				userVo.setPassword((String)resultDoc.get("password"));
				userVo.setGender((String)resultDoc.get("gender"));
				userVo.setJoinDate(WebUtil.getFormatDate((Date)resultDoc.get("join_date")));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			client.close();
		}
		return userVo;
	}
	// 사용자 추가
	public boolean insert(UserVo vo) {
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		Document resultDoc = null;
		UserVo userVo = null;		
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			Long no = updateCounter(db);
			Document doc = new Document()
					.append("no",no)				
					.append("name",vo.getName())				
					.append("email",vo.getEmail())
					.append("password",vo.getPassword())
					.append("gender",vo.getGender())
					.append("join_date", new Date());
			collection.insertOne(doc);
			result = true;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			client.close();
		}
		return result;		
	}

	// 사용자 정보 수정 
	public boolean update(UserVo vo) {
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			collection.findOneAndUpdate(Filters.eq("no",vo.getNo()),Updates.combine(
					Updates.set("name", vo.getName()),
					Updates.set("email",vo.getEmail()),
					Updates.set("password",vo.getPassword()),
					Updates.set("gender", vo.getGender())
			    ));
			result = true;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			client.close();
		}
		return result;	
	}
	public boolean select(UserVo vo) {
		return findByEmailAndPassword(vo) != null; 		
	}
	
}
