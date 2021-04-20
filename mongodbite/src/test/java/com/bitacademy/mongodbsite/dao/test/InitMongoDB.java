package com.bitacademy.mongodbsite.dao.test;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class InitMongoDB {
	// mongo db 초기화 
	public static void main(String[] args) {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = mongoClient.getDatabase("nam");
//		MongoCollection<Document> collection = database.getCollection("board");

		insertCounter(database);
		createIndex(database,true);
	}
	// 각 Collection들의 auto increment index 역할 해주는 collection의 생성, 초기화
	private static void insertCounter(MongoDatabase database) {
		MongoCollection<Document> collection = database.getCollection("counter");
		collection.insertOne(new Document("_id", "userid").append("seq", 0));
		MongoCollection<Document> gbcollection = database.getCollection("gbcounter");
		gbcollection.insertOne(new Document("_id", "userid").append("seq", 0));
		MongoCollection<Document> bcollection = database.getCollection("bcounter");
		bcollection.insertOne(new Document("_id", "userid").append("seq", 0));
	}
	// collection 별 인덱스 생성 
	private static void createIndex(MongoDatabase database,boolean isAscend) {
		int indexOrder = isAscend ? 1 : -1;
		MongoCollection<Document> board = database.getCollection("board");
		MongoCollection<Document> user = database.getCollection("user");
		MongoCollection<Document> guestbook = database.getCollection("guestbook");
		// index 생성
		board.createIndex(new Document("no", indexOrder));		
		user.createIndex(new Document("no", indexOrder));		
		guestbook.createIndex(new Document("no", indexOrder));		
	}
}
