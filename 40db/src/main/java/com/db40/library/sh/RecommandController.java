package com.db40.library.sh;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class RecommandController {
	
	private final Recommand recommandService;
	private final ObjectMapper objectMapper;
	
	@GetMapping("/books/recommandations")
	public String showRecommandations(Model model) {
		String recommandedBooksJson = recommandService.getRecommnadedBooks();
		List<RecommandBookInfo> bookList = Collections.emptyList();
		
		try {
		Map<String, Object> responseMap = objectMapper.readValue(recommandedBooksJson, new TypeReference<Map<String, Object>>() {});
		Object itemObject = responseMap.get("item");
		
		if (itemObject instanceof List) {
			bookList = objectMapper.convertValue(itemObject, new TypeReference<List<RecommandBookInfo>>() {});
		}
		 	model.addAttribute("recommandations", bookList);
		} catch (IOException e) {e.printStackTrace();
		  	model.addAttribute("recommandations", List.of() );
		}
		return "/books/recommand_books";
	}
}


