package com.db40.library.binary3300;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
//ttbwlstn33001258001

import org.springframework.stereotype.Component;

@Component
public class BookHopeApi {
		public String getApi(String query) throws IOException { // 사용자가 넣어주는 파라미터 (검색어)
	        String result = "";
	        // 1. 연결

	        String ttbKey = "ttbwlstn33001258001"; // 알라딘에서 발급받은 TTB Key

	        // 알라딘 API URL과 검색 파라미터 설정
	        String param = "?ttbkey=" + ttbKey 
	                     + "&Query=" + URLEncoder.encode(query, "UTF-8")
	                     + "&QueryType=Title&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101";
	        URL url = new URL("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx" + param);

	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        // 2. 옵션 설정
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");

	        // 3. 성공적으로 연결되었을 때 처리
	        if (conn.getResponseCode() == 200) {
	            StringBuffer buffer = new StringBuffer();
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line = "";
	            while ((line = br.readLine()) != null) {
	                buffer.append(line);
	            }
	            result = buffer.toString();
	            br.close();
	        }
	        conn.disconnect();
	        return result;

	}
}
