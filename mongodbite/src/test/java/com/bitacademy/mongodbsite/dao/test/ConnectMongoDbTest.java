package com.bitacademy.mongodbsite.dao.test;

import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.expr;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;
import static java.util.Arrays.asList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.bitacademy.mongodbsite.dao.BoardDao;
import com.bitacademy.mongodbsite.vo.BoardVo;
import com.bitacademy.web.mvc.WebUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Variable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class ConnectMongoDbTest {

	public static void main(String[] args) {
		mongoQuickStart();
	}

	public static void mongoQuickStart() {

		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = mongoClient.getDatabase("nam");
		MongoCollection<Document> collection = database.getCollection("board");

//		insertCounter(database);
//		insertGuestbookCounter(database);
//		insertBoardCounter(database);
//		createIndex(database);

//		addTest();

//		updateCounter(database);
//		insertDoc(collection);
//		insertManyDoc(collection);
		// count document
//		findDoc(collection);
//		findEqDoc(collection,0);
//		findGtDoc(collection,70);
//		findMaxVal(collection,70);

//		findRangeDoc(collection,30,50);
//		updateDoc(collection,0,5959);
//		updateManyDoc(collection,120,100);
//		deleteDoc(collection, 5959);
//		findEqDoc(collection,5959);
//		deleteManyDoc(collection, 200);
//		createIndex(collection, true);
//		joinCollection(database);
//		findDocByCondition(database,0L);

//		for(int i=0; i< 20; i++) {
//			addTest(i);
//		}
		System.out.println(collection.countDocuments());
//		findDocByStringMatch(collection,"파국");
		countByStringMatch(collection, "user", "jenkins");
//		findAllDoc(collection);

	}

	private static void countByStringMatch(MongoCollection<Document> collection, String column, String keyword) {
		long result = -1L;
//		.find(regex(column, ".*" + Pattern.quote(keyword)+".*"))
		if ("user".equals(column)) {
			MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
			MongoDatabase database = mongoClient.getDatabase("nam");
			MongoCollection<Document> user = database.getCollection("user");
			List<Document> docList = user.find(eq("name", keyword)).into(new ArrayList<Document>());
			if (docList.isEmpty()) {
				result = 0L;
			} else {
				Long userNo = docList.get(0).getLong("no");
				result = collection.countDocuments(eq("user_no", userNo));
			}
		} else {
			result = collection.countDocuments(regex(column, ".*" + Pattern.quote(keyword) + ".*"));
		}
		System.out.println(result);
	}

	private static void findDocByStringMatch(MongoCollection<Document> collection, String string) {
		BoardVo vo = null;
		List<Variable<String>> variable = asList(new Variable<>("uno", "$user_no"));
		List<Bson> pipeline = asList(match(expr(new Document("$eq", asList("$no", "$$uno")))),
				project(fields(include("name"), excludeId())));
		List<Document> documents = collection.aggregate(asList(
//						match(new Document("contents",asList("$in",string)))
				match(new Document("title", new BsonRegularExpression(".*" + string + ".*")))
//						match(new Document("title",new Document("$regex",".*"+Pattern.quote(string)+".*")))
//						match(new Document("title",new Document("$regex","/파국/").append("$options","gi")))
				, lookup("user", variable, pipeline, "user_name"), unwind("$user_name"),
				new Document("$addFields", new Document("user_name", "$user_name.name"))))
				.into(new ArrayList<Document>());

		for (Document doc : documents) {
			vo = new BoardVo();
			vo.setNo(doc.getLong("no"));
			vo.setUserNo(doc.getLong("user_no"));
			vo.setUserName(doc.getString("user_name"));
			vo.setTitle(doc.getString("title"));
			vo.setGroupNo(doc.getLong("group_no"));
			vo.setOrderNo(doc.getLong("order_no"));
			vo.setDepth(doc.getLong("depth"));
			vo.setContents((String) doc.get("contents"));
			vo.setRegDate(WebUtil.getFormatDate((Date) doc.get("reg_date")));
			vo.setViews(doc.getLong("views"));

			System.out.println(vo.toString());
		}
	}

	private static void findDocByCondition(MongoDatabase database, Long no) {
		MongoCollection<Document> collection = database.getCollection("board");
		BoardVo vo = null;
		System.out.println(no);
		List<Variable<String>> variable = asList(new Variable<>("uno", "$user_no"));
		List<Bson> pipeline = asList(match(expr(new Document("$eq", asList("$no", "$$uno")))),
				project(fields(include("name"), excludeId())));
		List<Document> documents = collection
				.aggregate(asList(match(eq("no", no)), lookup("user", variable, pipeline, "user_name"),
						unwind("$user_name"), new Document("$addFields", new Document("user_name", "$user_name.name"))))
				.into(new ArrayList<Document>());

		for (Document doc : documents) {
			vo = new BoardVo();
			vo.setNo(no);
			vo.setUserNo(doc.getLong("user_no"));
			vo.setUserName(doc.getString("user_name"));
			vo.setTitle(doc.getString("title"));
			vo.setGroupNo(doc.getLong("group_no"));
			vo.setOrderNo(doc.getLong("order_no"));
			vo.setDepth(doc.getLong("depth"));
			vo.setContents((String) doc.get("contents"));
			vo.setRegDate(WebUtil.getFormatDate((Date) doc.get("reg_date")));
			vo.setViews(doc.getLong("views"));
			System.out.println(vo.toString());
		}

	}

	//
	// join 하기
	private static void joinCollection(MongoDatabase database) {
		MongoCollection<Document> collection = database.getCollection("board");
		// TODO Auto-generated method stub

//      project(computed("user_name", "$name"));
		List<Variable<String>> variable = asList(new Variable<>("uno", "$user_no"));
		List<Bson> pipeline = asList(match(expr(new Document("$eq", asList("$no", "$$uno")))),
				project(fields(include("name"), excludeId())));
		// Bson pipe = lookup("user", variable, pipeline, "user_name");

		MongoCursor<Document> cur = collection.aggregate(asList(lookup("user", variable, pipeline, "user_name"),
				unwind("$user_name"), new Document("$addFields", new Document("user_name", "$user_name.name"))
//						,unwind("$user.name")
//						,unwind("$user.name")
		// ,project(fields(include("user.name","no","user_no","title","group_no",
		// "order_no","depth")))
		// ,unwind("$name.name",new UnwindOptions().includeArrayIndex("name"))
		)).iterator();
		// = collection.find().iterator();
		try {
			while (cur.hasNext()) {
				System.out.println(cur.next().toJson());
			}
		} finally {
			cur.close();
		}

	}

	// 인덱스 생성 (no)
	private static void createIndex(MongoDatabase database) {
		MongoCollection<Document> collection = database.getCollection("user");
		MongoCollection<Document> bcollection = database.getCollection("board");
		MongoCollection<Document> gbcollection = database.getCollection("guestbook");
		collection.createIndex(Indexes.ascending("no"));
		bcollection.createIndex(Indexes.ascending("no"));
		gbcollection.createIndex(Indexes.ascending("no"));
	}

	private static void addTest(int n) {
		BoardVo vo = new BoardVo();
		vo.setUserNo(1L);
		vo.setTitle("test" + n);
		vo.setContents("test 내용");
		new BoardDao().insertBoard(vo);
	}

	private static void aggregationTest() {

	}

	// 최대 값 찾기
	private static void findMaxVal(MongoCollection<Document> collection, int i) {
		Document doc = collection.find().sort(descending("no")).first();
		int no = doc.getInteger("group_no", 0) + 1;
//		int no = (int)doc.get("no");
		System.out.println(no);

		List<Document> list = collection.find().sort(orderBy(descending("group_no"), ascending("order_no"))).limit(5)
				.into(new ArrayList<Document>());

		Long order_no = (Long) (collection.find(eq("group_no", 1L)).sort(descending("order_no")).first()
				.get("order_no"));
		collection.findOneAndDelete(and(eq("group_no", 1L), eq("order_no", order_no)));
	}

	private static Long updateCounter() {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = mongoClient.getDatabase("nam");
		MongoCollection<Document> collection = database.getCollection("counter");
		Document docRevised = collection.findOneAndUpdate(eq("_id", "userid"), inc("seq", 1));
//		System.out.println((Long)docRevised.get("seq"));
		return (Long) docRevised.get("seq");
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

	// document 추가
	public static boolean insertDoc(MongoCollection<Document> collection) {
//		Document doc = new Document("name", "MongoDB").append("type", "database").append("count", 1)
//				.append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
//				.append("info", new Document("x", 203).append("y", 102));
		// user
//		Long no = updateCounter();
		boolean result = false;
		Document doc = new Document()
//				.append("no",no)				
				.append("name", "rick").append("email", "rick@gmail.com").append("password", "asdf")
				.append("gender", "male").append("join_date", new Date());
		try {
			collection.insertOne(doc);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 많은 document 추가
	public static void insertManyDoc(MongoCollection<Document> collection) {
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < 100; i++) {
			documents.add(new Document("i", i));
		}
		collection.insertMany(documents);
	}

	// 첫번째 document print
	public static void findDoc(MongoCollection<Document> collection) {
//		Document myDoc = collection.find().first();
//		System.out.println(myDoc.toJson());
		Document myDoc = collection.find(eq("name", "rck")).first();
		System.out.println(myDoc.get("_id"));
		System.out.println(myDoc.get("name"));
		System.out.println(myDoc.get("email"));
		System.out.println(myDoc.get("password"));
		System.out.println(myDoc.get("gender"));
		System.out.println(myDoc.get("join_date"));
		System.out.println(myDoc.get("join_date").getClass().getName());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(format.format(myDoc.get("join_date")));
	}

	// 값이 일치하는 document print
	public static void findEqDoc(MongoCollection<Document> collection, int val) {
		Document myDoc = collection.find(eq("i", val)).first();
		if (null != myDoc) {
			System.out.println(myDoc.toJson());
		} else {
			System.out.println("document not found");
		}
	}

	// 값보다큰 document print
	public static void findGtDoc(MongoCollection<Document> collection, int val) {
		collection.find(gt("i", val)).forEach(doc -> System.out.println(doc.toJson()));
	}

	// 지정한 범위 값의 document print(초과, 이하)
	public static void findRangeDoc(MongoCollection<Document> collection, int start, int end) {
		collection.find(and(gt("i", start), lte("i", end))).forEach(doc -> System.out.println(doc.toJson()));

	}

	// print all document
	public static void findAllDoc(MongoCollection<Document> collection) {
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
	}

	// update document
	public static void updateDoc(MongoCollection<Document> collection, int target, int value) {
//		collection.updateOne(Filters.eq("i", target),Updates.set("i", value));
		collection.findOneAndUpdate(eq("name", "zzz"), combine(set("name", "jenkins"),
				set("email", "jenkins@gmail.com"), set("password", "asdf"), set("gender", "female")));
	}

	// update multiple document
	public static void updateManyDoc(MongoCollection<Document> collection, int condition, int value) {
		UpdateResult updateResult = collection.updateMany(lt("i", condition), inc("i", value));
		System.out.println(updateResult.getModifiedCount());
		Long group_no = 1L, order_no = 1L;
		collection.updateMany(and(eq("group_no", group_no), gt("order_no", order_no)), inc("order_no", 1));

	}

	// delete document
	public static void deleteDoc(MongoCollection<Document> collection, int value) {
		collection.deleteOne(eq("i", value));
	}

	// delete multiple document
	public static void deleteManyDoc(MongoCollection<Document> collection, int condition) {
		DeleteResult deleteResult = collection.deleteMany(gte("i", condition));
		System.out.println(deleteResult.getDeletedCount());
	}

	// create index()
	public static void createIndex(MongoCollection<Document> collection, boolean isAscend) {
		int indexOrder = isAscend ? 1 : -1;
		collection.createIndex(new Document("i", indexOrder));
	}

}
