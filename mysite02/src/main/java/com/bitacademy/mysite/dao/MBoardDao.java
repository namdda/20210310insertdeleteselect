package com.bitacademy.mysite.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.bson.Document;

import com.bitacademy.mysite.vo.MBoardVo;
import com.bitacademy.mysite.vo.MPageVo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.google.gson.Gson;

public class MBoardDao {

	private static MBoardDao boardMDao = new MBoardDao();
	// Connect to MongoDB Server on localhost, port 27017 (default)
	private static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017")); 

	public static MBoardDao getInstance() {
		return boardMDao;
	}

	public static MongoClient getClient() {
		return mongoClient;
	}


	public List<MBoardVo> findAll(MPageVo pageVo) {
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		FindIterable<Document> docs = null;
		

		try {
			List<MBoardVo> list = new ArrayList<MBoardVo>();
			database = mongoClient.getDatabase("webdb");
			collection = database.getCollection("board");
			Document query = new Document();
			query.append("group_no", -1).append("order_no", 1);
			docs = collection.find().sort(query).skip((pageVo.getNowPage()-1)*pageVo.getCntPerPage()).limit(pageVo.getCntPerPage());
			
			for (Document doc : docs) {
				MBoardVo vo = new MBoardVo();
				System.out.println(doc);
				
				vo.setNo(doc.getLong("no"));
				vo.setTitle(doc.getString("title"));
				vo.setName(doc.getString("name"));
				vo.setUserNo(doc.getLong("userNo"));
				vo.setHit_cnt(doc.getInteger("hit_cnt"));
				vo.setReg_date(doc.getString("reg_date"));
				vo.setDepth(doc.getInteger("depth"));
				vo.setDel_gb(doc.getString("del_gb"));
				list.add(vo);
			}

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}


	public MBoardVo findOne(Long no) {
		MBoardVo vo = new MBoardVo();
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		FindIterable<Document> docs = null;

		try {
			database = mongoClient.getDatabase("webdb");
			collection = database.getCollection("board");
			Document query = new Document();
			query.append("no", no);
			docs = collection.find(query);					
			
			//6. 데이터 가져오기
			if(docs != null) {
				for(Document doc : docs) {
					vo = new MBoardVo();
					vo.setNo(doc.getLong("no"));
					vo.setTitle(doc.getString("title"));
					vo.setContent(doc.getString("content"));
					vo.setUserNo(doc.getLong("userNo"));
					vo.setGroup_no(doc.getInteger("group_no"));
					vo.setOrder_no(doc.getInteger("order_no"));
					vo.setDepth(doc.getInteger("depth"));
					// 조회수 증가
					updateHit_cnt(no);	
				}
			}
		}  catch (Exception ex) {
			//1. 사과
			//2. log
			System.out.println(ex);
		} 

		return vo;
	}

	public List<MBoardVo> search(String searchText, MPageVo pageVo) {
		List<MBoardVo> list = new ArrayList<MBoardVo>();
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		FindIterable<Document> docs = null;

		try {
			database = mongoClient.getDatabase("webdb");
			collection = database.getCollection("board");

			Document query = new Document();
			query.append("title", new Document().append("$regex", "/" + searchText + "/"))
			.append("content", new Document().append("$regex", "/"+ searchText + "/"))
			.append("del_gb", "N").append("group_no", -1).append("order_no", 1);

			docs = collection.find(query).skip((pageVo.getNowPage()-1)*pageVo.getCntPerPage()).limit(pageVo.getCntPerPage());



			for(Document doc : docs) {
				MBoardVo vo = new MBoardVo();
				vo.setNo(doc.getLong("no"));
				vo.setTitle(doc.getString("title"));
				vo.setName(doc.getString("name"));
				vo.setUserNo(doc.getLong("userNo"));
				vo.setHit_cnt(doc.getInteger("hit_cnt"));
				vo.setReg_date(doc.getString("reg_date"));
				vo.setDepth(doc.getInteger("depth"));
				list.add(vo);
				
				
				System.out.println(doc);
			}


		}  catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
		return list;
	}

	private boolean updateHit_cnt(Long no) {
		boolean result = true;
		int hit_cnt = 0;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoIterable<Document> docs = null;
		try {
			database = mongoClient.getDatabase("webdb");
			collection = database.getCollection("board");

			// 기존 조회수 가져오기

			Document prequery = new Document();
			prequery.append("no", no);

			docs = collection.find(prequery);
			for (Document doc : docs) {
				hit_cnt = doc.getInteger("hit_cnt");
			}


			// 조회수 update
			Document query = new Document();
			query.append("no", no);
			Document update = new Document();
			update.append("$set", new Document().append("hit_cnt", hit_cnt+1));
			collection.updateOne(query, update);

			//6. 결과
			//result = count == 1;
		}  catch (Exception ex) {
			//1. 사과
			//2. log
			System.out.println(ex);
		} finally {

		}

		return result;
	}


	public boolean insert(MBoardVo vo, String reply) {

		boolean result = true;
		int num=0;
		int group_no = 0;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> autoIncrement = null;
		MongoIterable<Document> docs = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar time = Calendar.getInstance();
		try {


			database = mongoClient.getDatabase("webdb");
			collection = database.getCollection("board");
			autoIncrement = database.getCollection("autoincrement");

			FindIterable<Document> auto = autoIncrement.find();


			if(autoIncrement.count() == 0) {
				autoIncrement.insertOne(new Document().append("idx", num));
			} else {
				for(Document idx : auto) {
					num = idx.getInteger("idx");
				}	
			}

			
			
			Document lastInsertGroupNo = new Document();
			lastInsertGroupNo.append("_id", -1);
			docs = collection.find().sort(lastInsertGroupNo).limit(1);
			if (collection.count() == 0) {
				group_no = 0;
			} else {
				for(Document doc : docs) {
					group_no = doc.getInteger("group_no")+1;
				}	
			}
			
			
			
			Document post = new Document();
			
			if(reply.equals("Y")) {
				post.append("no", (long) num)
				.append("title", vo.getTitle())
				.append("content", vo.getContent())
				.append("name", vo.getName())
				.append("userNo", vo.getUserNo())
				.append("hit_cnt", 0)
				.append("reg_date", format.format(time.getTime()))
				.append("group_no", vo.getGroup_no())
				.append("order_no", vo.getOrder_no())
				.append("depth", 0)
				.append("del_gb", "N");
			} else {
				post.append("no", (long) num)
				.append("title", vo.getTitle())
				.append("content", vo.getContent())
				.append("name", vo.getName())
				.append("userNo", vo.getUserNo())
				.append("hit_cnt", 0)
				.append("reg_date", format.format(time.getTime()))
				.append("group_no", group_no)
				.append("order_no", 1)
				.append("depth", 0)
				.append("del_gb", "N");
			}


			try {
				collection.insertOne(post);
				Document query = new Document();
				query.append("idx", num);
				Document docupdate = new Document();
				docupdate.append("$set", new Document().append("idx", num+1));
				autoIncrement.updateOne(query,docupdate);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(result && reply.equals("Y")) {
				setHierarchy(vo.getGroup_no(), vo.getOrder_no(), vo.getDepth());
			}

		}  catch (Exception ex) {
			System.out.println(ex);
		} finally {

		}
		return result;
	}

	// 계층 생성
	private boolean setHierarchy(int group_no, int order_no, int depth) {
		boolean result = true;
		Long getLastInsertNo = 0L;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCollection<Document> id = null;
		MongoIterable<Document> docs = null;

		try {
			database = mongoClient.getDatabase("webdb");
			collection = database.getCollection("board");
			

			// 계층 update
			Document hierarchyQuery = new Document();
			hierarchyQuery.append("group_no", group_no).append("depth", new Document().append("$gt", depth));
			collection.updateOne(hierarchyQuery, new Document().append("$set", new Document().append("depth", depth+1)));
			
			
			
			// 정렬 update
			Document orderQuery = new Document();
			orderQuery.append("group_no", group_no).append("order_no", new Document().append("$gt", order_no));
			collection.updateOne(orderQuery, new Document().append("$set", new Document().append("order_no", order_no+1)));
			
			
			
			//현재 답글의 order_no++
			
			Document lastInsertNo = new Document();
			lastInsertNo.append("_id", -1);
			docs = collection.find().sort(lastInsertNo).limit(1);
			for(Document doc : docs) {
				getLastInsertNo = doc.getLong("no");
			}
			
		
			Document curorderQuery = new Document();
			curorderQuery.append("no", getLastInsertNo);
			
			collection.updateMany(curorderQuery, new Document().append("$set", new Document().append("order_no", order_no+1).append("depth", depth+1)));
		} catch (Exception e) {

		}
		return result;
	}

	public boolean delete(Long no) {
		boolean result = true;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;		

		try {			
			database = mongoClient.getDatabase("webdb");
			collection = database.getCollection("board");

			Document query = new Document();
			query.append("no", no);
			Document setData = new Document();
			setData.append("del_gb", "Y");
			Document update = new Document();
			update.append("$set", setData);
			
			//collection.updateOne(query, update);
			
			collection.deleteOne(query);

		}  catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
		return result;
	}

	public boolean update(MBoardVo vo) {
		boolean result = true;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;	

		try {			
			database = mongoClient.getDatabase("webdb");
			collection = database.getCollection("board");

			Document query = new Document();
			query.append("no", vo.getNo());
			Document setData = new Document();
			setData.append("title", vo.getTitle()).append("content", vo.getContent());
			Document update = new Document();
			update.append("$set", setData);
			
			collection.updateOne(query, update);

			//result = count == 1;
		}  catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}

		return result;
	}

	public int getTotalCount() {
		int count = 0;
		MongoDatabase database = mongoClient.getDatabase("webdb");
		MongoCollection<Document> collection = database.getCollection("board");
		try {
			
			count = (int) collection.count();

		}  catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}

		return count;
	}


	public int searchTotalCount(String searchText) {
		int count = 0;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		try {
			database = mongoClient.getDatabase("webdb");
			collection = database.getCollection("board");

			Document query = new Document();
			query.append("title", new Document().append("$regex", "/" + searchText + "/"))
			.append("content", new Document().append("$regex", "/"+ searchText + "/"))
			.append("del_gb", "N");

			count = (int) collection.count(query);


		}  catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}

		return count;
	}


}
