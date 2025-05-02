package com.db40.library.yj.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.db40.library.sh.Books;
import com.db40.library.sh.Category;
import com.db40.library.yj.AdminBooksRepository;
import com.db40.library.yj.CategoryRepository;
import com.db40.library.yj.CategoryService;
@Component
public class BookApi{
	@Autowired
	AdminBooksRepository booksRepository;
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	CategoryService categoryService;
	private static final String BASE_URL = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx?";
	public String findBooks(String searchWord, String keyword,String ajax) {
		List<Books> bookResult = null;
		StringBuffer sb = new StringBuffer();
		try {	
			Map<String, String> hm = new HashMap<>();
			hm.put("ttbkey", "ttbduring42771204001");
			hm.put("Query", "");
			hm.put("Query", searchWord);
			hm.put("QueryType", keyword);
			hm.put("MaxResults", "20");
			hm.put("start", "1");
			hm.put("SearchTarget", "Book");
			hm.put("output", "js");
			hm.put("Version", "20131101");
		//#1. URL
		//		https://openapi.naver.com/v1/search/book.xml	XML
		//		https://openapi.naver.com/v1/search/book.json	JSON
//		String apiurl = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx?"
//				+ "ttbkey=ttbduring42771204001&"
//				+ "Query="+searchWord+"&"
//				+ "QueryType="+keyword+"&"
//				+ "MaxResults=10&"
//				+ "start=1&"
//				+ "SearchTarget=Book&"
//				+ "output=js&"
//				+ "Version=20131101";
		StringBuffer apiUrl = new StringBuffer();
		Iterator<String> iter = hm.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			String val = hm.get(key);
			apiUrl.append(key).append("=").append(val).append("&");
		}
		URL url = new URL(BASE_URL+apiUrl.toString());
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
		String line="";   

		while(  (line=br.readLine() )     != null ) {
			sb.append(line+"\n");  
		}
		
		br.close();   conn.disconnect(); 
		}catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
		
	}
	public List<Books> findBooks(String searchWord, String keyword) {
		List<Books> bookResult = null;
		try {	
			Map<String, String> hm = new HashMap<>();
			hm.put("ttbkey", "ttbduring42771204001");
			hm.put("Query", "");
			hm.put("Query", searchWord);
			hm.put("QueryType", keyword);
			hm.put("MaxResults", "10");
			hm.put("start", "1");
			hm.put("SearchTarget", "Book");
			hm.put("output", "js");
			hm.put("Version", "20131101");
		//#1. URL
		//		https://openapi.naver.com/v1/search/book.xml	XML
		//		https://openapi.naver.com/v1/search/book.json	JSON
//		String apiurl = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx?"
//				+ "ttbkey=ttbduring42771204001&"
//				+ "Query="+searchWord+"&"
//				+ "QueryType="+keyword+"&"
//				+ "MaxResults=10&"
//				+ "start=1&"
//				+ "SearchTarget=Book&"
//				+ "output=js&"
//				+ "Version=20131101";
		StringBuffer apiUrl = new StringBuffer();
		Iterator<String> iter = hm.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			String val = hm.get(key);
			apiUrl.append(key).append("=").append(val).append("&");
		}
		URL url = new URL(BASE_URL+apiUrl.toString());
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
		String line="";   
		StringBuffer sb = new StringBuffer();
		while(  (line=br.readLine() )     != null ) {
			sb.append(line+"\n");  
		}
		
		bookResult = getBookData(sb.toString());
		
		br.close();   conn.disconnect(); 
		
		
		//JsonFactory factory = JsonFactory.builder().build();
		//JsonParser parser = factory.createParser(sb.toString());
		
//		JsonObject jsonObejct = (JSONObject) jsonParser.par
		//while(parser.nextToken()
		
		//bookResult = getBookData(parser);
		
		//for(Books book : bookResult) {
		//	System.out.println("book.getCategory().getBookCategoryName()="+book.getCategory().getBookCategoryName());
		//}
        //parser.close();
		
	 }catch(Exception e) {e.printStackTrace();}
	  
	  return bookResult;
	}
	
	private List<Books> getBookData(String str) throws Exception{
		List<Books> bookList = new ArrayList<>();
		JSONParser parser = new JSONParser();
		JSONObject jsonObejct =  (JSONObject)parser.parse(str);
		JSONArray item = (JSONArray) jsonObejct.get("item");
		//String subCategoryName = categoryName.replace("국내도서>", "").substring(0,categoryName.replace("국내도서>", "").indexOf(">"));
		for(int i=0; i< item.size(); i++) {
			Books book = new Books();
			JSONObject itembody = (JSONObject) item.get(i);
			String categoryName = itembody.get("categoryName").toString();
			String subCategoryName = categoryName.replace("국내도서>", "").substring(0,categoryName.replace("국내도서>", "").indexOf(">"));
			Category category = categoryRepository.findByBookCategoryName(subCategoryName).get();
			
			//JSONObject body = (JSONObject) itembody.get("title");
			book.setBookAuthor(itembody.get("author").toString());
			book.setBookTitle(itembody.get("title").toString());
			book.setBookIsbn(itembody.get("isbn").toString());
			book.setBookDescription((itembody.get("description").toString()));
			book.setBookPublishedDate(itembody.get("pubDate").toString());
			book.setCategory( 
								category
					);
			book.setBookCover(itembody.get("cover").toString());
			book.setBookPublisher(itembody.get("publisher").toString());
			bookList.add(book);
		}
		return bookList; 
	}
	//private List<Books> getBookData(JsonParser parser) throws IOException{
//		if (parser.nextToken() != JsonToken.START_ARRAY) {
//        throw new IOException("Error: Expected data to start with an Object");
//    }
	//try {
        // [STEP5] JSONArray의 끝인 배열이 나올때까지 반복합니다.
       // while (parser.nextToken() != JsonToken.END_ARRAY) {

            // [STEP6] 시작 데이터가 Object인지 확인
          //  if (parser.currentToken() == JsonToken.START_OBJECT) {
                // [STEP7] Object 데이터 끝이 될때까지 반복합니다.
//		List<Books> bookList = new ArrayList<Books>();
//		List<Category> categoryList = new ArrayList<Category>();
//		while (parser.nextToken() != JsonToken.END_OBJECT) {
//            // [STEP8] JSON의 키 값을 가져옵니다.
//            parser.nextToken();
//            String fieldName = parser.getCurrentName();
//            Books book = new Books();
//            // [STEP9] JSON의 키 값을 기반으로 값을 추출합니다.
//            if ("title".equals(fieldName)) {
//                String title = parser.getValueAsString();
//                if(title.equals("link")) {
//                	continue;
//                }
//                book.setBookTitle(title);
//                System.out.println("title :: " + title);
//            } else if ("author".equals(fieldName)) {
//                String author = parser.getValueAsString();
//                book.setBookAuthor(author);
//                System.out.println("author :: " + author);
//            } else if ("description".equals(fieldName)) {
//                String bookIntro = parser.getValueAsString();
//                book.setBookIntro(bookIntro);
//                System.out.println("bookIntro :: " + bookIntro);
//            }else if ("isbn".equals(fieldName) || "isbn13".equals(fieldName)) {
//                String isbn = parser.getValueAsString();
//                book.setBookIsbn(isbn);
//                System.out.println("isbn :: " + isbn);
//            }else if ("pubDate".equals(fieldName) || "isbn13".equals(fieldName)) {
//                String bookPublishedDate = parser.getValueAsString();
//                book.setBookPublishedDate(bookPublishedDate);
//                System.out.println("bookPublishedDate :: " + bookPublishedDate);
//            }else if ("cover".equals(fieldName)) {
//                String bookCover = parser.getValueAsString();
//                book.setBookPublishedDate(bookCover);
//                System.out.println("bookCover :: " + bookCover);
//            }else if ("publisher".equals(fieldName)) {
//                String publisher = parser.getValueAsString();
//                book.setBookPublisher(publisher);
//                System.out.println("publisher :: " + publisher);
//            }else if ("categoryName".equals(fieldName)) {
//                String categoryName = parser.getValueAsString();
//                System.out.println("categoryName ="+categoryName);
//                System.out.println("cnt:"+categoryName.replace("국내도서>", ""));
//                System.out.println("indexof="+categoryName.replace("국내도서>", "").indexOf(">"));
//                System.out.println("replace= "+categoryName.replace("국내도서>", "").substring(0,categoryName.replace("국내도서>", "").indexOf(">")));
//                String subCategoryName = categoryName.replace("국내도서>", "").substring(0,categoryName.replace("국내도서>", "").indexOf(">"));
//                
//                Category category = categoryRepository.findByBookCategoryName(subCategoryName).get();
//                System.out.println("categoryName="+category.getBookCategoryName()+"/"+category.getBookCategoryNo());
//                //category.setBook_category_name(categoryName);
//                book.setCategory(category);
//                System.out.println("categoryName :: " + categoryName);
//            }
//            bookList.add(book);
//        }
//		return bookList;
//	}
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