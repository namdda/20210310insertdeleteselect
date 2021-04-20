package com.bitacademy.mongodbsite.pagination;

public class PagingBean {
	// 현재 페이지
   private int nowPage = 1;
   
   // 페이지 당 보여줄 게시물 수
   private int contentNumberPerPage = 5;
   
   //    페이지 그룹 당 페이지 수
   private int pageNumberPerPageGroup = 4;
   
   //    현재 db에 저장된 총 게시물 수
   private int totalContents;
   

   
   public PagingBean(){}

   //    총게시물수를 할당
   public PagingBean(int totalContents) {
       this.totalContents = totalContents;
   }

   // 총게시물수와 현재 페이지 정보를 할당
   public PagingBean(int totalContents, int nowPage) {
       this.totalContents = totalContents;
       this.nowPage = nowPage;
   }

// 현재 페이지 리턴
   public int getNowPage() {
       return nowPage;
   }
   // 현 페이지에서 보여줄 행(row) 의 시작번호
   public int getStartRowNumber() {
       return ((nowPage - 1) * contentNumberPerPage) + 1;
   }
   // 현 페이지에서 보여줄 행(row)의 마지막 번호
   public int getEndRowNumber() {
       int endRowNumber = nowPage * contentNumberPerPage;
       if (totalContents < endRowNumber)
           endRowNumber = totalContents;
       return endRowNumber;
   }
   // 총 페이지 수를 return한다.
   public int getTotalPage() {
       int totalPage = 0;
       if(totalContents % contentNumberPerPage == 0){
           totalPage = totalContents / contentNumberPerPage;
       }

       if(totalContents % contentNumberPerPage > 0){
           totalPage = (totalContents / contentNumberPerPage) + 1;
       }

       return totalPage;
   }
   // 총 페이지 그룹의 수를 return한다.
   public int getTotalPageGroup() {
       int totalPageGroup = 0;
       if(getTotalPage() % pageNumberPerPageGroup == 0){
           totalPageGroup = getTotalPage() / pageNumberPerPageGroup;
       }

       if(getTotalPage() % pageNumberPerPageGroup > 0){
           totalPageGroup = (getTotalPage() / pageNumberPerPageGroup) + 1;
       }
       return totalPageGroup;
   }   
   
   // 현재 페이지가 속한 페이지 그룹 번호(몇 번째 페이지 그룹인지) 을 return 하는 메소드
   public int getNowPageGroup() {
       int nowPageGroup = 0;
       if(getNowPage() % pageNumberPerPageGroup == 0){
           nowPageGroup = getNowPage() / pageNumberPerPageGroup;
       }
       if(getNowPage() % pageNumberPerPageGroup > 0){
           nowPageGroup = (getNowPage() / pageNumberPerPageGroup) + 1 ;
       }
       return nowPageGroup;
   }

   // 현재 페이지가 속한 페이지 그룹의 시작 페이지 번호를 return 한다.
   public int getStartPageOfPageGroup() {
       int startPageOfPageGroup;
       startPageOfPageGroup = (pageNumberPerPageGroup * (getNowPageGroup() -1)) + 1;
       return startPageOfPageGroup;
   }
   
   // 현재 페이지가 속한 페이지 그룹의 마지막 페이지 번호를 return 한다.
   public int getEndPageOfPageGroup() {
       int endPageOfPageGroup = pageNumberPerPageGroup * getNowPageGroup();

       if(endPageOfPageGroup > getTotalPage()){
           endPageOfPageGroup = getTotalPage();
       }
       return endPageOfPageGroup;
   }

   // 이전 페이지 그룹이 있는지 체크하는 메서드
   public boolean isPreviousPageGroup() {
       boolean flag = false;
       if(getNowPageGroup() > 1){
           flag = true;
       }
       return flag;
   }

   // 다음 페이지 그룹이 있는지 체크하는 메서드
   public boolean isNextPageGroup() {
       boolean flag = false;
       if(getNowPageGroup() < getTotalPageGroup()){
           flag = true;
       }
       return flag;
   }
	
	public int getContentNumberPerPage() {
		return contentNumberPerPage;
	}
	
	public int getTotalContents() {
		return totalContents;
	}

	public void setTotalContents(int totalContents) {
		this.totalContents = totalContents;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public void setContentNumberPerPage(int contentNumberPerPage) {
		this.contentNumberPerPage = contentNumberPerPage;
	}

	// 페이징 시 글 번호 표시를 위한  
	public int getLastIndexOfNextPage() {
		if(getNowPage() == getTotalPage()) {
			return 0;
		}
		int remains = getTotalPage() % getContentNumberPerPage() + 1; 
		return (getTotalPage() - getNowPage()-1) * contentNumberPerPage + remains;						
	}
   
}

