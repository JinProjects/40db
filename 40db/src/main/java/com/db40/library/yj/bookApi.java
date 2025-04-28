package com.db40.library.yj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.db40.library.sh.Books;
import com.db40.library.sh.Category;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class bookApi {
	public static void main(String[] args) {
	  try {	
		//#1. URL
		//		https://openapi.naver.com/v1/search/book.xml	XML
		//		https://openapi.naver.com/v1/search/book.json	JSON
		String apiurl = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbduring42771204001&Query=java&QueryType=Title&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101";
		URL url = new URL(apiurl);
		//#2. HttpURLConnection
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//#3. 요청파라미터
		conn.setRequestMethod("GET");
		//conn.setRequestProperty("X-Naver-Client-Id", "reu63fdeQl8IXmwLVsRM");
		//conn.setRequestProperty("X-Naver-Client-Secret", "AvEnm8FcX2"); 
		
		//#4. 응답코드 - 200
		//System.out.println(conn.getResponseCode());
		int code = conn.getResponseCode();
		BufferedReader br;
		if(  code == 200) { br = new BufferedReader(new InputStreamReader(conn.getInputStream()));}
		else { br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));}
		//#5. 응답
		String line="";   StringBuffer sb = new StringBuffer();
		while(  (line=br.readLine() )     != null ) {
			sb.append(line+"\n");  
		}
		System.out.println(sb.toString());
		br.close();   conn.disconnect(); 
		JsonFactory factory = JsonFactory.builder().build();
		JsonParser parser = factory.createParser(sb.toString());
//		if (parser.nextToken() != JsonToken.START_ARRAY) {
//	        throw new IOException("Error: Expected data to start with an Object");
//	    }
		//try {
	        // [STEP5] JSONArray의 끝인 배열이 나올때까지 반복합니다.
	       // while (parser.nextToken() != JsonToken.END_ARRAY) {

	            // [STEP6] 시작 데이터가 Object인지 확인
	          //  if (parser.currentToken() == JsonToken.START_OBJECT) {

	                // [STEP7] Object 데이터 끝이 될때까지 반복합니다.
					
	                while (parser.nextToken() != JsonToken.END_OBJECT) {
	                    // [STEP8] JSON의 키 값을 가져옵니다.
	                    String fieldName = parser.getCurrentName();
	                    parser.nextToken();
	                    Books book = new Books();
	                    // [STEP9] JSON의 키 값을 기반으로 값을 추출합니다.
	                    if ("title".equals(fieldName)) {
	                        String title = parser.getValueAsString();
	                        if(title.equals("link")) {
	                        	continue;
	                        }
	                        book.setBookTitle(title);
	                        System.out.println("title :: " + title);
	                    } else if ("author".equals(fieldName)) {
	                        String author = parser.getValueAsString();
	                        book.setBookAuthor(author);
	                        System.out.println("author :: " + author);
	                    } else if ("description".equals(fieldName)) {
	                        String bookIntro = parser.getValueAsString();
	                        //book.setBookDescription(bookIntro);
	                        System.out.println("bookIntro :: " + bookIntro);
	                    }else if ("isbn".equals(fieldName) || "isbn13".equals(fieldName)) {
	                        String isbn = parser.getValueAsString();
	                        book.setBookIsbn(isbn);
	                        System.out.println("isbn :: " + isbn);
	                    }else if ("pubDate".equals(fieldName) || "isbn13".equals(fieldName)) {
	                        String bookPublishedDate = parser.getValueAsString();
	                        book.setBookPublishedDate(bookPublishedDate);
	                        System.out.println("bookPublishedDate :: " + bookPublishedDate);
	                    }else if ("cover".equals(fieldName)) {
	                        String bookCover = parser.getValueAsString();
	                        book.setBookPublishedDate(bookCover);
	                        System.out.println("bookCover :: " + bookCover);
	                    }else if ("publisher".equals(fieldName)) {
	                        String publisher = parser.getValueAsString();
	                        book.setBookPublisher(publisher);
	                        System.out.println("publisher :: " + publisher);
	                    }else if ("categoryName".equals(fieldName)) {
	                        String categoryName = parser.getValueAsString();
	                        Category category = new Category();
	                     //   category.setBook_category_name(categoryName);
//	                        book.setCategory(
//	                        		category
//	                        		);
	                        System.out.println("categoryName :: " + categoryName);
	                    }
	                }
	           // }
	                parser.close();
	       //}
	  //  } catch (IOException e) {
	      //  e.printStackTrace();
	   // }
		
	 }catch(Exception e) {e.printStackTrace();}
	//}
	}
}
//https://developers.naver.com/docs/serviceapi/search/book/book.md#%EC%B1%85
/*
#1.
Client ID		reu63fdeQl8IXmwLVsRM
Client Secret	AvEnm8FcX2

#2. URL
https://openapi.naver.com/v1/search/book.xml	XML
https://openapi.naver.com/v1/search/book.json	JSON

#3. 요청파라미터
GET    / query 
> X-Naver-Client-Id: {애플리케이션 등록 시 발급받은 클라이언트 아이디 값}
> X-Naver-Client-Secret: {애플리케이션 등록 시 발급받은 클라이언트 시크릿 값}
*/