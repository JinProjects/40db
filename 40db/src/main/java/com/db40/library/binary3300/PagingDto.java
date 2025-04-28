package com.db40.library.binary3300;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PagingDto {
	private int listtotal; 		//#1 전체글 122
	private int onepagelist; 	//#2 한 페이지에 보여줄 게시글의 수 10
	private int pagetotal; 		//#3 총페이지수 122/10 = 13 12페이지+2글
	private int bottomlist; 	//#4 하단의 페이지 나누기 이전 11 12 13 ----
	
	private int pstartno; 		//#5 페이지의 스타트번호 [1]:최신글 0~10개

	private int current; 		//#6 현재페이지번호
	private int start; 			//#6 시작페이지번호
	private int end; 			//#6 끝페이지번호
	
	
	public PagingDto(int listtotal, int pstartno) {
		super();
		this.listtotal = listtotal; //전체 계산한 값 123
		this.onepagelist = 10;
		
		//전체글 0일때
		if(this.listtotal<=0) {this.listtotal=1;}
		
		//총 페이지수 계산
		this.pagetotal = (int) Math.ceil(listtotal/(double)onepagelist);
		
		//하당페이지 버튼개수
		this.bottomlist=10;
		
		//페이지 시작번호
		this.pstartno = pstartno*10; 
		
		//     1  2  3  4   5  6  7  8  9  10 다음
		//이전 11 12 13 14 [15] 16 17 18 19 20 다음
		//pstartno=10이라고하면 현제페이지번호는 2
		//pstartno=40이라고하면 현제페이지번호는 5
		this.current = (this.pstartno/this.onepagelist)+1;
		
		//시작) 11~20까지의 숫자중 어떤걸 받아도 11이 되도록, 21~30까지의 숫자중 어떤걸 받아도 21이되도록.
		this.start = ((this.current-1)/this.bottomlist)*this.bottomlist+1;
		
		//끝) 
		this.end = this.start+bottomlist-1;
		if(pagetotal < end) {this.end = pagetotal;}
		
	}
	
}
