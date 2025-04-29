package com.db40.library.yj;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db40.library.yj.util.BookApi;

@Controller
public class AdminBooksController {
	@Autowired
	public BookApi bookApi;
	
	@Autowired
	public AdminBooksRepository bookRepository;
	
	@GetMapping("/books/bookDelete/{bookNo}")
	public String bookDelete(@PathVariable("bookNo") String bookNo) {
		bookRepository.deleteById(Long.valueOf(bookNo));
		return "redirect:/admin/booksManage";
	}
	
	@GetMapping(value = "/books/findBook/{searchWord}/{selectKeyword}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String bookJson(@PathVariable String searchWord, @PathVariable String selectKeyword
							, @RequestParam(value="ajax", required = false) String ajax) {
		
		return bookApi.findBooks(searchWord, selectKeyword, ajax);
	}
	
	@PostMapping("/admin/bookSearch")
	public String bookSearch(HttpServletRequest request) {
		String keyword = (String)request.getAttribute("booksSearch");
		System.out.println("keyword ="+keyword );
		return "";
	}
}
