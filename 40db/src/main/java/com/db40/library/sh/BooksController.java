 	package com.db40.library.sh;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BooksController {

    private final BooksService booksService;


    @GetMapping("books/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Books> paging = this.booksService.getList(page);
        model.addAttribute("paging", paging);
        return "/books/book_search";
    }
    
	@GetMapping(value = "books/detail/{bookNo}")
	public String detail (Model model, @PathVariable("bookNo") Long bookNo) {
		Books books = this.booksService.getBooks(bookNo);
		model.addAttribute("books" , books);
		return "/books/book_detail";
	}
	
	@GetMapping("/books/search")
	public String searchBooks (@RequestParam(value = "page" , defaultValue = "0") int page, 
											   @RequestParam(value = "keyword" , defaultValue= "") String keyword,
											   Model model) {
		
		Page<Books> paging = this.booksService.getSearch(page, keyword);
		model.addAttribute("paging", paging);
		model.addAttribute("keyword", keyword);
		return "/books/book_search";
		
	}

}