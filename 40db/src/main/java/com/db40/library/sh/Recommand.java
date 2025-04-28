package com.db40.library.sh;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class Recommand {
	
	 @Value("${aladin.api.key}")
	private String apiKey;
	 
	private final RestTemplate restTemplate;
	
	public Recommand(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public String getRecommnadedBooks() {
		String url = "https://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbserenial871425001&QueryType=ItemNewAll&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101";
		String response = restTemplate.getForObject(url, String.class);
		return response;	
	}

}
