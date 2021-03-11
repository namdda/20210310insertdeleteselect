<%@page import="com.bitacademy.guestbook.vo.GuestbookVo"%>
<%@page import="com.bitacademy.guestbook.dao.GuestbookDao"%>
<%
	String no = request.getParameter("no");
String password = request.getParameter("password");

GuestbookVo vo = new GuestbookVo();
vo.setNo(Long.parseLong(no));
vo.setPassword(password);

GuestbookDao dao = new GuestbookDao();
dao.delete(vo);

//redirect는 request 값을 유지하지 않는다고 한다 

//Redirect는 Web Container로 명령이 들어오면, 웹 브라우저에게 다른 페이지로 이동하라고 명령을 내립니다. 
//그러면 웹 브라우저는 URL을 지시된 주소로 바꾸고 해당 주소로 이동합니다. 
//다른 웹 컨테이너에 있는 주소로 이동하며 새로운 페이지에서는 Request와 Response객체가 새롭게 생성됩니다.
//그러므로 처음 보냈던 최초의 Request와 Response 객체는 유효하지 않고 새롭게 생성되는 것 입니다.

/*
사용자가 보낸 요청 정보를 이용하여 글쓰기 기능을 수행한다고 할 때, 
redirect를 사용하여 응답 페이지를 부르면 사용자가 실수 혹은 고의로 
글쓰기 응답 페이지에서 새로고침을 누른다고 하더라도, 
처음의 요청 정보는 존재하지 않으므로 게시물이 여러 번 등록되지 않습니다.
*/

//그렇기 때문에 시스템에 변화가 생기는 요청(회원가입, 글쓰기 등)의 경우에는 redirection을 사용하는 것이 바랍직합니다.
response.sendRedirect("/guestbook01/index.jsp");
%>