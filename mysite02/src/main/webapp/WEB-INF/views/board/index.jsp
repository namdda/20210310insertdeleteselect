
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link
	href="${pageContext.servletContext.contextPath }/assets/css/board.css"
	rel="stylesheet" type="text/css">

<style type="text/css">
#tmTable tr:nth-child(even) {
	background-color: #f2f2f2;
}
</style>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form"
					action="${pageContext.request.contextPath }/board" method="post">
					<input type="text" id="kwd" name="kwd" value="${param.kwd}">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex" id="tmTable">
					<tr>
						<th>번호</th>
						<th>글번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					<%-- <c:set var="count" value="${fn:length(list)}" />--%>

					<c:forEach items="${data}" var="vo" varStatus="no">

						<tr>
							<td>${start+no.index }</td>

							<td>${vo.no}</td>


							<%-- <td style="text-align:Left; padding-left:${(vo.depth-1)*40}">  --%>
							<td style="text-align: Left;"><c:if test="${vo.depth>0}">
									<c:forEach begin="1" end="${vo.depth}" step="1">
									 &nbsp;
								 </c:forEach>
									<img
										src="${pageContext.request.contextPath}/assets/images/reply.png">
								</c:if> <a onclick="setHistroy(this);getDate()"
								href="${pageContext.request.contextPath}/board?a=view&no=${vo.no}">
									${vo.title} </a></td>
							<td>${vo.userName }</td>
							<td>${vo.cnt }</td>
							<td>${vo.regDate }</td>
							<td><c:if test="${authUser.no == vo.userNo }">
									<a
										href="${pageContext.request.contextPath}/board?a=delete&no=${vo.no}"
										class="del">삭제</a>
								</c:if></td>
						</tr>
					</c:forEach>
				</table>






				<!-- pager 추가 -->

				<div class="pager">
					<c:if test="${empty param.kwd}">
						<ul>


							<c:if test="${startPage>1 }">
								<li><a href="?nowPage=${startPage-1 }"> ◀ </a></li>
							</c:if>
							<c:forEach begin="${startPage }" end="${endPage}" step="1"
								var="i">
								<c:choose>
									<c:when test="${nowPage==i }">
										<li class="selected">${i }</li>
									</c:when>
									<c:otherwise>
										<li><a href="?nowPage=${i }">${i }</a></li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:if test="${endPage<lastPage }">
								<li><a href="?nowPage=${endPage+1 }"> ▶ </a></li>
							</c:if>
						</ul>
					</c:if>
				</div>
				<!-- pager 추가 -->

				<c:if test="${not empty authUser}">
					<div class="bottom">
						<a href="${pageContext.request.contextPath}/board?a=writeform"
							id="new-book">글쓰기</a>
					</div>
				</c:if>
			</div>

			<div id="histroy">
			

				<table id="his" border="1" class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>링크</th>
					</tr>
				</table>
				<button id="clearStorage" onclick="clear_storage();">clear storage</button>
			</div>
				
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board" />
		</c:import>

		<c:import url="/WEB-INF/views/includes/footer.jsp" />

	</div>


	<script>
		var histroy = JSON.parse(localStorage.getItem("histroy")) || [];
		var date;
		
		//console.log(histroy);
		window.onload = function() {
			getHistroy();
		}

		function setHistroy(a) {
			var url = a.href;
			histroy.push(url);
			localStorage.setItem("histroy", JSON.stringify(histroy));
		}
		
		
		

		function getHistroy() {
			var ptable = document.getElementById("his");
			var cnt = 1;
			
			for (var i = 0; i < histroy.length; i++) {
			
				
				
				var Node = document.createTextNode(histroy[i]);
							
				var tr = document.createElement("tr");
				ptable.appendChild(tr);
				
				var notd =document.createElement("td");
				var number = document.createTextNode(cnt);
				notd.appendChild(number);
				tr.appendChild(notd);
				cnt++;
				
				var linktd = document.createElement("td");
				linktd.appendChild(Node);
				tr.appendChild(linktd);
				

			}
		}
		
		function clear_storage(){
			
			 localStorage.clear();
			 window.location.reload();

		}

	</script>

</body>
</html>