package com.db40.library.yj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db40.library.yj.admin.AdminRepository;
import com.db40.library.yj.util.BookApi;

@Controller
public class AdminBooksController {
	@Autowired
	public BookApi bookApi;
	
	@Autowired
	public AdminBooksRepository bookRepository;
	@Autowired
	public AdminRepository adminRepository;
	
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
	
}
