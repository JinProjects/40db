package com.db40.library.member;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class GoogleLogin {
	@Value("${google_client_id}")
	private String google_client_id;
	
	@Value("${google_client_secret}")
	private String google_client_secret;
	
	@Value("${google_redirect_uri}")
	private String google_redirect_uri;
	
	// 인증
	public String identify() {
		return "https://accounts.google.com/o/oauth2/v2/auth?response_type=code"
				+ "&scope=openid%20email"
				+ "&client_id="+ google_client_id
				+ "&redirect_uri="+ google_redirect_uri;
	}
	
	// 토큰 받기
	public List<String> identified(String code) {
	    System.out.println("identify : " + code);

	    String userUrl = "https://oauth2.googleapis.com/token";
	    String accessToken = "";
	    StringBuilder buffer = new StringBuilder();

	    try {
	        String params = "grant_type=authorization_code"
	                + "&client_id=" + URLEncoder.encode(google_client_id, "UTF-8")
	                + "&client_secret=" + URLEncoder.encode(google_client_secret, "UTF-8")
	                + "&redirect_uri=" + URLEncoder.encode(google_redirect_uri, "UTF-8")
	                + "&code=" + URLEncoder.encode(code, "UTF-8");

	        URL url = new URL(userUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

	        try (OutputStream os = conn.getOutputStream()) {
	            os.write(params.getBytes("UTF-8"));
	        }

	        BufferedReader br = new BufferedReader(new InputStreamReader(
	                conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream(), "UTF-8"));

	        String line;
	        while ((line = br.readLine()) != null) {
	            buffer.append(line);
	        }
	        br.close();

	        accessToken = buffer.toString();
	        System.out.println("응답 받은 accessToken JSON: " + accessToken);

	        JsonObject job = JsonParser.parseString(accessToken).getAsJsonObject();
	        String token = job.get("access_token").getAsString();
	        System.out.println("접근 토큰: " + token);

	        return userinformation(token);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}

	
	public List<String> userinformation(String token) {
		List<String>  userinfos = new ArrayList<>();
		String userurl = "https://www.googleapis.com/userinfo/v2/me";
		
		try {
	        URL url = new URL(userurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Authorization", "Bearer " + token);

	        BufferedReader br;
	        if (conn.getResponseCode() == 200) {
	            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }

	        StringBuilder response = new StringBuilder();
	        String line;
	        while ((line = br.readLine()) != null) {
	            response.append(line);
	        }

	        br.close();
	        conn.disconnect();

	        System.out.println("사용자 정보 응답: " + response);

	        JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();
	        userinfos.add(json.get("email").getAsString());

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		System.out.println(userinfos);
		
		return userinfos;
		
	}
}





















