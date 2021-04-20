<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="footer">
	<p>(c)opyright 2021</p>

</div>
<script>
	//저장
	window.onbeforeunload = function(event) {
		var url = window.location.href;
		var browserHistory = JSON.parse(localStorage.getItem('history'));
		if (browserHistory == undefined) {
			browserHistory = [];
		}
		browserHistory.push(url);
		localStorage.setItem('history', JSON.stringify(browserHistory));
	}
	
	// 이벤트 처리 함수(태그별 다른 처리)
	var tagClickEvent = function(event) {		
		//event.preventDefault();
		event.stopPropagation();
		event.stopImmediatePropagation();
		console.log();
				
		// tag 이름 저장
		var elem = event.target;		
		var clickString = "";
		console.log(elem.tagName);		
		var tag = elem.tagName; 
		clickString += tag + " TAG clicked ";
		
		// 따로 처리하는 tag  
		if (elem.tagName == 'A' || elem.tagName == 'TH' || elem.tagName == 'TD' || elem.tagName == 'LI') {
			//clickString += elem.href + " ";
			clickString += elem.innerHTML.trim() + " ";			
			console.log(elem.innerHTML.trim());
		}

		// local storage에 저장
		var clickHistory = JSON.parse(localStorage.getItem('clickHistory'));
		if (clickHistory == undefined) {
			clickHistory = [];
		}
		clickHistory.push(clickString);
		localStorage.setItem('clickHistory', JSON.stringify(clickHistory));
		
	}
	
	// tag 별 event 처리 함수 등록
	var aTags = document.getElementsByTagName('A');
	var tdTags = document.getElementsByTagName('TD');
	var thTags = document.getElementsByTagName('TH');
	var liTags = document.getElementsByTagName('LI');
	
	Array.from(aTags).forEach(function(element, index, array){ element.onclick = tagClickEvent; });
	Array.from(tdTags).forEach(function(element, index, array){ element.onclick = tagClickEvent; });
	Array.from(thTags).forEach(function(element, index, array){ element.onclick = tagClickEvent; });
	Array.from(liTags).forEach(function(element, index, array){ element.onclick = tagClickEvent; });

</script>