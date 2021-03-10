<%@page import="com.bitacademy.guestbook.vo.GuestbookVo"%>
<%@page import="com.bitacademy.guestbook.dao.GuestbookDao"%>
<%
	//request() response() -> 잘 생각해보자 클라이언트 쪽에서 넣어달라고 db 쪽에 request 하는거다.
	request.setCharacterEncoding("utf-8");
	String name = request.getParameter("name");
	String password = request.getParameter("password");
	String contents = request.getParameter("contents");
	
	
	
	//줄바꿈? 일단 값을 받아온 뒤에, 거기에 \n이 있을테니 받아온 값에 따라 적당히 바꿔치기 해라 라는 뜻
	contents = contents.replaceAll("\n", "<br/>");
	
	GuestbookVo vo = new GuestbookVo();
	vo.setName(name);
	vo.setPassword(password);
	vo.setContents(contents);
	
	GuestbookDao dao = new GuestbookDao();
	dao.insert(vo);
	
	//redirect는 지금 먹던 집에서 마치고 새 집으로 2차 가는 것과 같다고 했다. 즉, 이건 새 페이지로 넘어간다는 뜻
	//foward는 주소 변화는 없고 값 변화는 있다 
	//Web Container 차원에서 페이지의 이동만 존재합니다. 
	//실제로 웹 브라우저는 다른 페이지로 이동했음을 알 수 없습니다. 
	//그렇기 때문에 웹 브라우저에는 최초에 호출한 URL이 표시되고, 이동한 페이지의 URL 정보는 확인할 수 없습니다. 
	//또한 현재 실행중인 페이지와 forward에 의해 호출될 페이지는 Request 객체와 Response 객체를 공유합니다. 
	
	//request response 어렵다. request 요구하기, resposne 반응하기로만 익히는걸론 한계가 느껴진다. 
	
	//그렇기 때문에 게시판을 제작하는 과정에서는 시스템에 변화가 생기지 않는 단순 조회 요청(글 목록 보기, 검색)
	//forward로 응답하는 것이 바람직합니다.
	response.sendRedirect("/guestbook01/index.jsp"); //주소 변경은 있고 값 변화는 없다
%>