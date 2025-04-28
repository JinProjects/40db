package com.db40.library.member;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class NaverLogin {
	@Value("${naver_redirect_url}")
	private String naver_redirect_url;
	@Value("${client_id}")
	private String client_id;
	@Value("${client_secret}")
	private String client_secret;
	private String state = "test";
	
	protected String identified_token;

	// 인증
	public String identify() {
		return "https://nid.naver.com/oauth2.0/authorize?response_type=code"
				+ "&client_id="+ client_id
				+ "&state="+ state
				+ "&redirect_uri="+ naver_redirect_url;
	}

	// 토큰 인가
	public List<String> identified(String code, String state) {
		System.out.println("identify : "+code);
		System.out.println("state : "+state);
	    String usernUrl = "https://nid.naver.com/oauth2.0/token"
	    		+ "?grant_type=authorization_code"
	    		+ "&client_id="+ client_id
	    		+ "&client_secret="+ client_secret
	    		+ "&code="+ code
	    		+ "&state="+ state;

	    URL url = null;   HttpURLConnection conn = null; 
		BufferedReader br = null; String line="";   StringBuffer buffer=new StringBuffer();
		String accessToken="";
		
		try {
			url  = new URL(usernUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);   conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			
			if(conn.getResponseCode() == 200) {
			    br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else { br=new BufferedReader(new InputStreamReader(conn.getErrorStream())); }
			
			while(  (line = br.readLine())  != null    ) { buffer.append(line); }
			br.close();
			conn.disconnect();
			accessToken = buffer.toString();
			System.out.println("아이디 : "+client_id);
			System.out.println("시크릿 : "+client_secret);
			System.out.println("접근 토큰 : "+accessToken);
		} catch (MalformedURLException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }
		
		JsonObject job = JsonParser.parseString(accessToken).getAsJsonObject();
		String token   = job.get("access_token").getAsString();
		System.out.println("추출된 토큰 : "+token);
		identified_token = token;
		return userinfo(token);
	}
	
	// 유저 정보 받기
	public List<String> userinfo(String token) {
		
		List<String> infos = new ArrayList<>();
		String userUrl = "https://openapi.naver.com/v1/nid/me";
		
		URL url = null;   HttpURLConnection conn = null; 
		BufferedReader br = null; String line="";   StringBuffer buffer=new StringBuffer();
		String result="";
		
		try {
			url  = new URL(userUrl);
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setRequestProperty("Authorization", "Bearer " +token);  //##
 
			if(conn.getResponseCode() == 200) {
			    br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else { br=new BufferedReader(new InputStreamReader(conn.getErrorStream())); }
			
			while(  (line = br.readLine())  != null    ) { buffer.append(line); }
			br.close();
			conn.disconnect();
			 
		} catch (MalformedURLException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }
		
		result = buffer.toString(); System.out.println("결과 : "+result);
		JsonObject job = JsonParser.parseString(result).getAsJsonObject();
		infos.add(job.getAsJsonObject("response").get("name").getAsString());
		infos.add(job.getAsJsonObject("response").get("email").getAsString());
		infos.add(job.getAsJsonObject("response").get("nickname").getAsString());
		infos.add(job.getAsJsonObject("response").get("gender").getAsString());
		infos.add(job.getAsJsonObject("response").get("birthday").getAsString());
		infos.add(job.getAsJsonObject("response").get("birthyear").getAsString());
		infos.add(job.getAsJsonObject("response").get("mobile").getAsString());
	
		
		System.out.println(infos.toString());
		return infos;
	}
	
	public void unlink() {
		
		String token = identified_token;
		identified_token="";
		
		String userUrl = "https://nid.naver.com/oauth2.0/token"
				+ "?grant_type=delete"
				+ "&client_id="+client_id
				+ "&client_secret="+client_secret
				+ "&access_token="+token;
		
		URL url = null;   HttpURLConnection conn = null; 
		BufferedReader br = null; String line="";   StringBuffer buffer=new StringBuffer();
		String result="";
		
		try {
			url  = new URL(userUrl);
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setRequestProperty("Authorization", "Bearer " +token);
 
			if(conn.getResponseCode() == 200) {
			    br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else { br=new BufferedReader(new InputStreamReader(conn.getErrorStream())); }
			
			while(  (line = br.readLine())  != null    ) { buffer.append(line); }
			br.close();
			conn.disconnect();
			 
		} catch (MalformedURLException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }
		
		result = buffer.toString(); System.out.println("결과 : "+result.toString());
		
	}
}



































