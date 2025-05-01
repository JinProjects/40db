package com.db40.library.yj;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db40.library.member.Member;
import com.db40.library.sh.Books;
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
	
	@PostMapping("/admin/booksSearch")
	public String booksSearch(HttpServletRequest request, Model model) {
		String keyword = request.getParameter("booksSearch");
		System.out.println("keyword = "+keyword);
		List<Books> findBooksList =	bookRepository.findByTitleOrAuthor(keyword);
		model.addAttribute("list",findBooksList);
		model.addAttribute("active","booksManage");
		return "/admin/booksManage";
	}
	@PostMapping("/admin/membersSearch")
	public String membersSearch(HttpServletRequest request, Model model) {
		String keyword = request.getParameter("membersSearch");
		System.out.println("keyword = "+keyword);
		List<Member> findMemberList = adminRepository.findAllByMemberIdOrderbyIdDesc(keyword);
		model.addAttribute("list",findMemberList);
		model.addAttribute("active","membersManage");
		return "/admin/membersManage";
	}
}
