package jstlel;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class _03Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<UserVo> list = new ArrayList<>();
		
		UserVo vo1 = new UserVo();
		vo1.setNo(10L);
		vo1.setName("메이메이");
		list.add(vo1);
		
		UserVo vo2 = new UserVo();
		vo2.setNo(20L);
		vo2.setName("이선");
		list.add(vo2);
		
		UserVo vo3 = new UserVo();
		vo3.setNo(30L);
		vo3.setName("루루");
		list.add(vo3);
		
	
		request.setAttribute("list", list);
		request.getRequestDispatcher("/WEB-INF/views/03.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
