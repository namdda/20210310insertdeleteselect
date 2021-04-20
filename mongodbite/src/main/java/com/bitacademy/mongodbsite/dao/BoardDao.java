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
import java.util.regex.Pattern;

import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.bitacademy.mongodbsite.pagination.PagingBean;
import com.bitacademy.mongodbsite.vo.BoardVo;
import com.bitacademy.mongodbsite.vo.GuestbookVo;
import com.bitacademy.web.mvc.WebUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Variable;
import com.mongodb.internal.client.model.AggregationLevel;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static java.util.Arrays.*;

public class BoardDao {
	
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
		return database.getCollection("board");
	}
	
	// auto increment를 대신하는 counter collection 
	private static Long updateCounter(MongoDatabase database) {
		MongoCollection<Document> collection = database.getCollection("bcounter");
		Document docRevised = collection.findOneAndUpdate(eq("_id","userid"),inc("seq",1));
		return Long.valueOf(docRevised.get("seq").toString());
	}
	// 게시물 입력 (답글 제외)
	public boolean insertBoard(BoardVo vo) {
		/*
		 * 원본 SQL
		 * 			sql =  "insert into board(user_no, title, group_no, order_no, "
				+ " depth, contents, reg_date) "
				+ "	values(?, ?, ifnull((select max(group_no)+1 from board b),1), 1, "
				+ " 0, ?, now());";

		 */
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			Long no = updateCounter(db);
			Long order_no = WebUtil.intToLong(1);
			Document groupNoDoc = collection.find().sort(descending("group_no")).first();
			Long group_no = groupNoDoc != null ? groupNoDoc.getLong("group_no") + 1 : 1;
			Long depth = WebUtil.intToLong(0);
			Long views = depth;
			Document doc = new Document()
					.append("no", no)
					.append("user_no",vo.getUserNo())
					.append("title", vo.getTitle())
					.append("group_no", group_no)
					.append("order_no", order_no)
					.append("depth", depth)
					.append("contents", vo.getContents())
					.append("reg_date", new Date())
					.append("views", views);
			collection.insertOne(doc);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}
	
	public boolean insertBoardReply(BoardVo originVo, BoardVo vo) {
		/*
			원본 SQL
 			// 답글 작성시 먼저 수행해야할 구문
			// 같은 그룹 에서 원글보다 나중에 달린 글들의 순서를 하나씩 증가시켜서 답글이 달릴 자리(순서)를 확보한다. 
			sql = "update board "
					+ "	set order_no = order_no + 1 "
					+ " where group_no = ? and order_no >= ? + 1;";
			
			// insert 수행 (원글의 순서, 깊이를 이용)
			sql =  "insert into board(user_no, title, group_no, order_no, "
					+ " depth, contents, reg_date) "
					+ "	values(?, ?, ?, ?+1, "
					+ " ?+1, ?, now());";
		 */
		boolean result = false;
		
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			// 답글 작성시 먼저 수행해야할 구문
			// 같은 그룹 에서 원글보다 나중에 달린 글들의 순서를 하나씩 증가시켜서 답글이 달릴 자리(순서)를 확보한다. 
			collection.updateMany(and(
					eq("group_no", originVo.getGroupNo())
					,gt("order_no", originVo.getOrderNo()))
					,inc("order_no", 1));
			
			// insert 수행 (원글의 순서, 깊이를 이용)
			Long no = updateCounter(db);
			Document doc = new Document()
					.append("no", no)
					.append("user_no",vo.getUserNo())
					.append("title", vo.getTitle())
					.append("group_no", originVo.getGroupNo())
					.append("order_no", originVo.getOrderNo()+1)
					.append("depth", originVo.getDepth()+1)
					.append("contents", vo.getContents())
					.append("reg_date", new Date())
					.append("views", 0L);
			collection.insertOne(doc);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}
	// 게시물 리스팅(게시판)
	public List<BoardVo> getBoardPageList(PagingBean pagingBean){
		/*
		 * 원본 SQL
		 * 			sql =  "select b.no, b.user_no, b.title, b.group_no, b.order_no, b.depth, "
					+ " date_format(b.reg_date,'%Y-%m-%d %H:%i:%s'), views, u.name "
					+ "	from board b "
					+ " join user u "
					+ " on b.user_no = u.no "					
					+ "	order by group_no DESC, order_no ASC"
					+ " LIMIT ?, ? ;";
		 */
		
		List<BoardVo> list = new ArrayList<BoardVo>();

		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		BoardVo vo = null;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			// aggregation 수행 	(유사 left outer join) 		
			// lookup용 변수 , 파이프라인 준비  
			// user_no 변수를 uno 로 사용
			List<Variable<String>> variable = asList(new Variable<>("uno", "$user_no"));
			List<Bson> pipeline = asList(
					// 조건에 맞는 document filter 
					match(
							expr(
									new Document("$eq", asList("$no", "$$uno"))
								)
						)
					// 결과물에서 name field만 추출, id 제외
					,project(
							fields(include("name"),excludeId())
							)
					);
			// 페이징 관련 부분
			List<Document> docList = null;
			int skipNum = pagingBean.getStartRowNumber()-1; 
			if( skipNum >0) {
				docList = collection.aggregate(
						asList(
								// 찾아볼 collection(right collection) / left collection에서 사용할 변수 / 변수가 사용될 pipeline / output field name
								lookup("user", variable, pipeline, "user_name")
								// user_name 배열을 해체 ( [1,2,3] -> 1 / 2 / 3 다른 document로 분리)
								,unwind("$user_name")
								// 새로운 field 추가 - nesting 된 field를 최상위 field에 재지정
								,new Document("$addFields",new Document("user_name","$user_name.name"))
								// group number 별 내림차순 & order number 오름차순
								,sort(orderBy(descending("group_no"),ascending("order_no")))
								// 지정한 글 개수 만큼 skip - paging 처리
								,skip(skipNum)
								// 페이지당 최대 5개의 게시물로 제한
								,limit(5)
							)
						).into(new ArrayList<Document>());
			// 제일 마지막 페이지는 skip 추가하지 않고 수행
			}else {
				docList = collection.aggregate(
						asList(
								lookup("user", variable, pipeline, "user_name")
								,unwind("$user_name")
								,new Document("$addFields",new Document("user_name","$user_name.name"))
								,sort(orderBy(descending("group_no"),ascending("order_no")))
								,limit(5)
							)
						).into(new ArrayList<Document>());
			}
			
			
			for(Document doc : docList) {
				vo = new BoardVo();
				long no = (long)(doc.get("no")); 
				vo.setNo(no);
				vo.setUserNo( doc.getLong("user_no"));
				vo.setUserName( doc.getString("user_name"));
				vo.setTitle( doc.getString("title"));
				vo.setGroupNo( doc.getLong("group_no"));
				vo.setOrderNo( doc.getLong("order_no"));
				vo.setDepth( doc.getLong("depth"));
				vo.setContents((String) doc.get("contents"));
				vo.setRegDate(WebUtil.getFormatDate((Date) doc.get("reg_date")));
				vo.setViews( doc.getLong("views"));
				list.add(vo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return list;
	}
	// 답글이 달린 원 게시글은 삭제하지 않는다.
	
	public boolean deleteBoard(BoardVo vo) {
		/*
		 * 			// 해당 그룹 내에서 답글이 없는 글(마지막 순서) 만 삭제 가능하다.
			sql = " delete from board "
					+ "where no = ? and order_no = (select max(order_no) from (select order_no from board where group_no = ?) b) ;";
		 */
		
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			// 해당 그룹 내에서 답글이 없는 글(마지막 순서) 만 삭제 가능하다.
			Long group_no = vo.getGroupNo();
			Long order_no = (Long)(collection.find(eq("group_no",group_no))
					.sort(descending("order_no"))
					.first().get("order_no"));
			// 가장 마지막 순서는 삭제 가능
			if(order_no == vo.getOrderNo()) {
				System.out.println("마지막 순서");
				collection.findOneAndDelete(eq("no",vo.getNo()));
			// 답글이 없는 글은 삭제 가능
			}else {
				Document nextDoc = collection.find(and(eq("group_no",group_no),eq("order_no",vo.getOrderNo()+1))).first();
				// 바로 밑에 위치한 글이 현재글의 답글이 아니라면 삭제
				if(nextDoc.getLong("depth") != vo.getDepth() + 1) {
					System.out.println("답글없음");
					collection.findOneAndDelete(eq("no",vo.getNo()));
				}
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
		
	}
	
	// 조회수 증가
	public boolean updateBoardViews(Long no) {
//					sql = "update board "
//							+ "	set views = views + 1"
//							+ " where no = ?;";
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			collection.findOneAndUpdate(eq("no",no),inc("views",1));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}
	
	public BoardVo getBoard(Long no) {
		System.out.println(no);
		/*
		 게시물 전체 내용 가져오기
			sql =  " select b.no, b.user_no, b.title, b.group_no, b.order_no, "
			" b.depth, b.contents, date_format(b.reg_date,'%Y-%m-%d %H:%i:%s'), views "
			"	from board b "
			" join user u on b.no = ? and b.user_no = u.no;";		
		 */
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		BoardVo vo = null;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			// 게시물 1개로 제한해서 가져온다. 
			List<Variable<String>> variable = asList(new Variable<>("uno", "$user_no"));
			List<Bson> pipeline = asList(
					match(
							expr(
									new Document("$eq", asList("$no", "$$uno"))
								
							)
						)
					,project(
							fields(include("name"),excludeId())
							)
					);
			List<Document> documents = collection.aggregate(
					asList(
							match(eq("no",no))
							,lookup("user", variable, pipeline, "user_name")
							,unwind("$user_name")
							,new Document("$addFields",new Document("user_name","$user_name.name"))
							,limit(1)
						)
					).into(new ArrayList<Document>());
			
			for(Document doc : documents) {
				vo = new BoardVo();
				vo.setNo(no);
				vo.setUserNo( doc.getLong("user_no"));
				vo.setUserName( doc.getString("user_name"));
				vo.setTitle( doc.getString("title"));
				vo.setGroupNo( doc.getLong("group_no"));
				vo.setOrderNo( doc.getLong("order_no"));
				vo.setDepth( doc.getLong("depth"));
				vo.setContents((String) doc.get("contents"));
				vo.setRegDate(WebUtil.getFormatDate((Date) doc.get("reg_date")));
				vo.setViews( doc.getLong("views"));
				//System.out.println(vo.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}		
		
		
		return vo;
	}
	
	public boolean updateBoard(BoardVo vo) {
//		sql = "update board "
//				+ "	set title = ?, contents = ? "
//				+ " where no = ?;";
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			collection.findOneAndUpdate(eq("no",vo.getNo()),combine(
					set("title",vo.getTitle())
					,set("contents",vo.getContents())));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}
	
	public List<BoardVo> searchBoardListByKeyword(PagingBean pagingBean, String column, String keyword){
		/*
		 * 			sql =  "select b.no, b.user_no, b.title, b.group_no, b.order_no, b.depth, "
					+ " date_format(b.reg_date,'%Y-%m-%d %H:%i:%s'), views, u.name "
					+ "	from board b "
					+ " join user u "
					+ " on b.user_no = u.no "; 
			// column 입력 값 별 질의문 차별화
			if("user".equals(column)) {
				sql += " and u.name like ? ";
			}else if("title".equals(column)) {
				sql += " and b.title like concat('%',?,'%') ";
			}else if("contents".equals(column)) {
				sql += " and b.contents like concat('%',?,'%') ";
			}else {
				sql += " and b.title like concat('%',?,'%') ";
			}
			sql += "	order by group_no DESC, order_no ASC"
					+ " LIMIT ?, ? ;";
		 */
		List<BoardVo> list = new ArrayList<BoardVo>();

		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		BoardVo vo = null;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			// aggregation 수행 
			
//			lookup용 변수 , 파이프라인 준비  
			List<Variable<String>> variable = asList(new Variable<>("uno", "$user_no"));
			List<Bson> pipeline = asList(
					match(
							expr(
									new Document("$eq", asList("$no", "$$uno"))
								)
						)
					,project(
							fields(include("name"),excludeId())
							)
					);
			// 페이징 관련 부분
			List<Document> docList = null;
			int skipNum = pagingBean.getStartRowNumber()-1; 
			//System.out.println(skipNum);
			
			// 사용자 이름 검색 시
			if("user".equals(column)) {
				if( skipNum >0) {
					docList = collection.aggregate(
							asList(								
									// 찾아볼 collection / 사용할 변수 / 변수가 사용될 pipeline / output field name
									lookup("user", variable, pipeline, "user_name")
									// user_name 배열을 해체 ( [1,2,3] -> 1 / 2 / 3 다른 document로 분리
									,unwind("$user_name")
									// 새로운 field 추가 - nesting 된 field를 상위 field에 재지정
									,new Document("$addFields",new Document("user_name","$user_name.name"))
									,match(new Document("user_name",new BsonRegularExpression(keyword)))
									// group number 별 내림차순 & order number 오름차순
									,sort(orderBy(descending("group_no"),ascending("order_no")))
									// 지정한 글 개수 만큼 skip - paging 처리
									,skip(skipNum)
									,limit(5)
									)
							).into(new ArrayList<Document>());				
				}else {
					docList = collection.aggregate(
							asList(
									lookup("user", variable, pipeline, "user_name")
									,unwind("$user_name")
									,new Document("$addFields",new Document("user_name","$user_name.name"))
									,match(new Document("user_name",new BsonRegularExpression(keyword)))
									,sort(orderBy(descending("group_no"),ascending("order_no")))
									,limit(5)
									)
							).into(new ArrayList<Document>());
				}				
			// 본문 , 제목 검색 시
			}else{
				
				if( skipNum >0) {
					docList = collection.aggregate(
							asList(								
									match(new Document(column,new BsonRegularExpression(".*"+keyword+".*")))
									// 찾아볼 collection / 사용할 변수 / 변수가 사용될 pipeline / output field name
									,lookup("user", variable, pipeline, "user_name")
									// user_name 배열을 해체 ( [1,2,3] -> 1 / 2 / 3 다른 document로 분리
									,unwind("$user_name")
									// 새로운 field 추가 - nesting 된 field를 상위 field에 재지정
									,new Document("$addFields",new Document("user_name","$user_name.name"))
									// group number 별 내림차순 & order number 오름차순
									,sort(orderBy(descending("group_no"),ascending("order_no")))
									// 지정한 글 개수 만큼 skip - paging 처리
									,skip(skipNum)
									,limit(5)
									)
							).into(new ArrayList<Document>());				
				}else {
					docList = collection.aggregate(
							asList(
									match(new Document(column,new BsonRegularExpression(".*"+keyword+".*")))
									,lookup("user", variable, pipeline, "user_name")
									,unwind("$user_name")
									,new Document("$addFields",new Document("user_name","$user_name.name"))
									,sort(orderBy(descending("group_no"),ascending("order_no")))
									,limit(5)
									)
							).into(new ArrayList<Document>());
				}
			}
			
			
			for(Document doc : docList) {
				vo = new BoardVo();
				long no = (long)(doc.get("no")); 
				vo.setNo(no);
				vo.setUserNo( doc.getLong("user_no"));
				vo.setUserName( doc.getString("user_name"));
				vo.setTitle( doc.getString("title"));
				vo.setGroupNo( doc.getLong("group_no"));
				vo.setOrderNo( doc.getLong("order_no"));
				vo.setDepth( doc.getLong("depth"));
				vo.setContents((String) doc.get("contents"));
				vo.setRegDate(WebUtil.getFormatDate((Date) doc.get("reg_date")));
				vo.setViews( doc.getLong("views"));
				list.add(vo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return list;
		
	}

//		// 게시물 전체 내용 가져오기
	
	public int selectBoardListCnt() {
//		https://gangnam-americano.tistory.com/18
//		sql =  " select count(no) "
//				+ "	from board;";
//		
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		long result = -1L;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			result = collection.countDocuments();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return WebUtil.longToInt(result);
	}
	// 검색어 조건에 맞는 게시글 수를 가져온다. 
	
	public int selectBoardListCnt(String column, String keyword) {
		
		/*
		 * 			// 게시물 전체 내용 가져오기
			sql =  " select count(1) "
					+ "	from board "; 
			if("title".equals(column)){
				sql += " where title like concat('%',?,'%');";				
			}else if("contents".equals(column)) {
				sql += " where contents like concat('%',?,'%');";				
			}else if("user".equals(column)) {
				sql += " b join user u on b.user_no = u.no "
					+ " and u.name like ?;";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		 */
//		return result;
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		long result = -1L;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			// 유저 이름은 전부 일치 , 제목/내용은 포함하는 것만 필터링하도록 설정 
			if("user".equals(column)) {
				MongoCollection<Document> userCollection = getCollection(db,"user");
				List<Document> docList = userCollection.find(eq("name",keyword)).into(new ArrayList<Document>());
				if(docList.isEmpty()) {
					result = 0L;
				}else {
					Long userNo = docList.get(0).getLong("no");
					result = collection.countDocuments(eq("user_no", userNo ));							
				}
			}else {
				result = collection.countDocuments(regex(column, ".*" + Pattern.quote(keyword)+".*"));			
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
//		System.out.println(result);
		return WebUtil.longToInt(result);
	}
}
