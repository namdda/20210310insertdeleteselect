package com.bitacademy.web.mvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {
	public static void forward(String path, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(path);
		// request, response를 그대로 forwarding 한다.
		rd.forward(request, response);
	}

	public static void redirect(String url, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.sendRedirect(url);
	}
	// url 에 들어갈 한글 parameter를 encoding 해준다.
	public static String urlEncode(String value, String charset) throws UnsupportedEncodingException {
	    return URLEncoder.encode(value, charset);
	}
	// Date 객체를 해당 날짜 형식에 맞게 표현 
	public static String getFormatDate(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	// Integer -> Long으로 변환 
	public static Long intToLong(int intValue) {
		return Long.parseLong(String.valueOf(intValue));
	}
	// Long -> Integer 으로 변환 
	public static int longToInt(long longValue) {
		return Integer.parseInt(String.valueOf(longValue));
	}
}
