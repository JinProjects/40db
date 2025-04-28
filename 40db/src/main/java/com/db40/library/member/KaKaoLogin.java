package com.db40.library.member;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class KaKaoLogin {
	@Value("${kakao_redirect_url}") private String kakao_redirect_url;
	
	@Value("${kakao_api}")
	private String kakao_api;
	
	protected String identified_token;
	
	// 1. login -  인증(사용자신원확인) , 인가(접근권한)
	public String  identify() {
		return  "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
					+ kakao_api
				    + "&redirect_uri="
					+ kakao_redirect_url;
	}
	// 2. 인가코드를 토큰
	public List<String> identified(String code) {  // code
		System.out.println("........ STEP1) " + code);
		String  tokenUrl="https://kauth.kakao.com/oauth/token";
		tokenUrl +=  "?grant_type=authorization_code" +"&client_id=" + kakao_api 
					 +"&redirect_uri=" + kakao_redirect_url +"&code=" + code;
		
		URL url = null;   HttpURLConnection conn = null; 
		BufferedReader br = null; String line="";   StringBuffer buffer=new StringBuffer();
		String resultToken="";
		
		try {
			url  = new URL(tokenUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);   conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			
			if(conn.getResponseCode() == 200) {
				    br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else { br=new BufferedReader(new InputStreamReader(conn.getErrorStream())); }
			
			while(  (line = br.readLine())  != null    ) { buffer.append(line); }
			br.close();
			conn.disconnect();
			resultToken = buffer.toString();
			System.out.println("............." + resultToken);
		} catch (MalformedURLException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }
		
		JsonObject job = JsonParser.parseString(resultToken).getAsJsonObject();
		String token   = job.get("access_token").getAsString();
		System.out.println("접근 토큰 :" + token);
		identified_token = token;
		return userinformation(token);
	} 

	public  List<String> userinformation(String token) {
		List<String>  userinfos = new ArrayList<>();
		
		String userUrl="https://kapi.kakao.com/v2/user/me";
		
		URL url = null;   HttpURLConnection conn = null; 
		BufferedReader br = null; String line="";   StringBuffer buffer=new StringBuffer();
		String result="";
		
		try {
			url  = new URL(userUrl);
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.setRequestProperty("Authorization", "Bearer " + token);  //##
 
			if(conn.getResponseCode() == 200) {
			    br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else { br=new BufferedReader(new InputStreamReader(conn.getErrorStream())); }
			
			while(  (line = br.readLine())  != null    ) { buffer.append(line); }
			br.close();
			conn.disconnect();
			 
		} catch (MalformedURLException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }
		
		
		result = buffer.toString();   System.out.println("result : " + result);
		JsonObject job = JsonParser.parseString(result).getAsJsonObject();
		userinfos.add(job.getAsJsonObject("properties").get("nickname").getAsString());
		
		userinfos.add(job.getAsJsonObject("kakao_account").get("email").getAsString()); 
		userinfos.add(job.getAsJsonObject("kakao_account").get("name").getAsString()); 
		userinfos.add(job.getAsJsonObject("kakao_account").get("gender").getAsString());
		userinfos.add(job.getAsJsonObject("kakao_account").get("phone_number").getAsString());
		userinfos.add(job.getAsJsonObject("kakao_account").get("birthday").getAsString()); //0901
		userinfos.add(job.getAsJsonObject("kakao_account").get("birthyear").getAsString()); //2001
		userinfos.add(job.get("id").getAsString());
		System.out.println("info : " + userinfos);
		return userinfos;
	}
	
	public void unlink() {
		
		String token = identified_token;
		identified_token="";
		String userUrl = "https://kapi.kakao.com/v1/user/unlink";
		
		URL url = null;   HttpURLConnection conn = null; 
		BufferedReader br = null; String line="";   StringBuffer buffer=new StringBuffer();
		String diconnectedUserId = "";
		
		try {
			url  = new URL(userUrl);
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.setRequestProperty("Authorization", "Bearer " + token);  //##
 
			if(conn.getResponseCode() == 200) {
			    br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else { br=new BufferedReader(new InputStreamReader(conn.getErrorStream())); }
			
			while(  (line = br.readLine())  != null    ) { buffer.append(line); }
			br.close();
			conn.disconnect();
			 
		} catch (MalformedURLException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }
		
		diconnectedUserId = buffer.toString(); System.out.println("연결해제됨 : "+ diconnectedUserId);
	}
}