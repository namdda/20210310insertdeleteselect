
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
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form" action="" method="post">
					<input type="text" id="kwd" name="kwd" value=""> <input
						type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					<c:set var="count" value="${fn:length(list)}" />
					<c:forEach items="${list}" var="vo" varStatus="status">
						<tr>
							<td>${count - status.index}</td>
							<td style="text-align:Left; padding-left:${(vo.depth-1)*40}">
								<a
								href="${pageContext.request.contextPath}/board?a=view&no=${vo.no}">
									<c:if test="${vo.depth > 0 }">
										<img
											src="${pageContext.request.contextPath}/assets/images/reply.png">
									</c:if> ${vo.title}
							</a>
							</td>
							<td>${vo.userName }</td>
							<td>${vo.cnt }</td>
							<td>${vo.regDate }</td>
							<td><c:if test="${authUser.no == vo.userNo }">
									<a
										href="${pageContext.request.contextPath}/board?a=delete&no=${vo.no}"
										class="del">삭제</a>
								</c:if></td>
							<!-- 						</tr>	 -->
					</c:forEach>
				</table>

				<!-- pager 추가 -->
				<div class="pager">
					<!-- 
					<ul>
					<c:if test="${startPage>1 }">
						<li><a href="?nowPage=${startPage-1 }"> ◀ </a></li>
					</c:if>
					<c:forEach begin="${startPage }" end="${endPage}" step="1" var="i">
						<c:choose>
							<c:when test="${nowPage==i }"><li class="selected">${i }</li></c:when>
							<c:otherwise>
								<li><a href="?nowPage=${i }">${i }</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${endPage<lastPage }">
						<li><a href="?nowPage=${endPage+1 }"> ▶ </a></li>
					</c:if>
					</ul>
				 -->
					<ul>
						<li><a href="">◀</a></li>
						<li><a href="">1</a></li>
						<li class="selected">2</li>
						<li><a href="">3</a></li>
						<li>4</li>
						<li>5</li>
						<li><a href="">▶</a></li>
					</ul>
				
				 
				</div>
				<!-- pager 추가 -->
				<c:if test="${not empty authUser}">
					<div class="bottom">
						<a href="${pageContext.request.contextPath}/board?a=writeform"
							id="new-book">글쓰기</a>
					</div>
				</c:if>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board" />
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>