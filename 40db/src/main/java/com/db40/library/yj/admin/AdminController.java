package com.db40.library.yj.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db40.library.member.Member;
import com.db40.library.member.MemberStatus;
import com.db40.library.member.MemberStatusRepository;
import com.db40.library.sh.Books;
import com.db40.library.sh.Category;
import com.db40.library.yj.AdminBooksRepository;
import com.db40.library.yj.AdminBooksService;
import com.db40.library.yj.CategoryRepository;
import com.db40.library.binary3300.*;

@Controller
public class AdminController {
	
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	AdminService adminService;
	
	@Autowired
	MemberStatusRepository memberStatusRepository;
	@Autowired
	AdminBooksService booksService;
	@Autowired
	AdminBooksRepository booksRepository;
	@Autowired
	CategoryRepository categoryRepository;
//	@Autowired
//	ChatGPTService chatGPTService;
	
	@GetMapping("/admin/main")
	public String test(Model model) {
		//model.addAttribute("list", bookApi.findBooks("java"));

		return "redirect:/admin/membersManage";
	}
	
	@GetMapping("/admin/membersManage")
	public String membersManage(Model model, @RequestParam( value="page" , defaultValue="0")	int page) {
		List<Member> memberList = adminRepository.findAll();
		
		model.addAttribute("list"   , adminService.memberGetPaging(page));  //10개씩
		//System.out.println("........" + this.service.findAll().size());
		model.addAttribute("paging" , new PagingDto(memberList.size() , page)   );  //##전체리스트뽑고

		for(Member member : memberList) {
			MemberStatus memberStatus = memberStatusRepository.findById(member.getMemberStatus().getId()).get();
			member.setMemberStatus(memberStatus);
		}
		
		model.addAttribute("active", "memberManage");
		model.addAttribute("memberList",memberList);
		return "admin/membersManage";
	}
	@PostMapping("/admin/membersSearch")
	public String membersSearch(HttpServletRequest request, Model model, @RequestParam( value="page" , defaultValue="0")	int page) {
		String keyword = request.getParameter("membersSearch");
		System.out.println("keyword = "+keyword);
		//List<Member> findMemberList = adminRepository.findAllByMemberIdOrderbyIdDesc(keyword);
		
		model.addAttribute("list"   , adminService.searchMemberGetPaging(page,keyword));  //10개씩
		//System.out.println("........" + this.service.findAll().size());
		model.addAttribute("paging" , new PagingDto(1 , page)   );  //##전체리스트뽑고

		/* model.addAttribute("list",findMemberList); */
		model.addAttribute("active","memberManage");
		System.out.println("실행");
		return "admin/membersManage";
	}
	@PostMapping("/admin/memberUpdate")
	public String memberUpdate(@RequestParam String memberId, @RequestParam String statusVal) {
		System.out.println("memberId="+memberId);
		Member member = adminRepository.findByMemberId(memberId).orElseThrow(()->new RuntimeException("회원을 찾을 수 없습니다."+memberId));
		MemberStatus status = new MemberStatus();
		status.setId(Integer.parseInt(statusVal));
		member.setMemberStatus(status);
		
		adminRepository.save(member);
		
		return "redirect:membersManage";
	}
//	@PostMapping("/admin/memberUpdate/${status}")
//	@ResponseBody
//	public List<> memberUpdate(String status) {		
//		return "";
//	}
	
	//======bookManage=================================================================
	
	@GetMapping("/admin/booksManage")
	public String booksManage(Model model, @RequestParam( value="page" , defaultValue="0")	int page) {
		List<Books> bookList = booksRepository.findAllByOrderByBookNoDesc();
		model.addAttribute("list"   , adminService.bookGetPaging(page));  //10개씩
		//System.out.println("........" + this.service.findAll().size());
		model.addAttribute("paging" , new PagingDto(bookList.size() , page)   );  //##전체리스트뽑고

		model.addAttribute("bookList", bookList);
		model.addAttribute("active", "booksManage");
		return "admin/booksManage";
	}
	
	@PostMapping("/admin/booksSearch")
	public String booksSearch(HttpServletRequest request, Model model, @RequestParam( value="page" , defaultValue="0")	int page) {
		List<Books> bookList = booksRepository.findAllByOrderByBookNoDesc();
		String keyword = request.getParameter("booksSearch");
		model.addAttribute("list"   , adminService.searchBookGetPaging(page,keyword));  //10개씩
		//System.out.println("........" + this.service.findAll().size());
		model.addAttribute("paging" , new PagingDto(1 , page)   );  //##전체리스트뽑고

		//model.addAttribute("bookList", bookList);
		model.addAttribute("active","booksManage");
		return "/admin/booksManage";
	}
	//도서등록
	@PostMapping("/admin/insertBook")
	@ResponseBody
	public ResponseEntity<?> insertBook(@RequestBody Books book, @RequestParam String bookCategoryName) {
		System.out.println("bookCategoryName:"+bookCategoryName);
		System.out.println("getBookIsbn:"+book.getBookIsbn());
		
		try {
		Category category = categoryRepository.findByBookCategoryName(bookCategoryName)
											.orElseThrow(()-> new RuntimeException("카테고리를 찾을 수 없습니다: "+book.getCategory().getBookCategoryName()));
		category.setBookCategoryName(bookCategoryName);
		book.setCategory(category);
		
		int cnt = booksRepository.findByIsbn(book.getBookIsbn());
		String msg = "exist";
		if(cnt == 0) {
			msg = "success";
			booksRepository.save(book);
		}
		return ResponseEntity.ok(Map.of("status",msg));
		}catch (Exception e) {
				e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status","error"));
		}
	}
	//도서 중복체크 후 등록
	@PostMapping("/admin/existBookReg")
	@ResponseBody
	public ResponseEntity<?> existBookReg(@RequestBody Books book, @RequestParam String bookCategoryName){
		try {
			Category category = categoryRepository.findByBookCategoryName(bookCategoryName).orElseThrow(()->new RuntimeException("카테고리를 찾을 수 없습니다."+book.getCategory().getBookCategoryName()));
			book.setCategory(category);
			booksRepository.save(book);
			return ResponseEntity.ok(Map.of("status","success"));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status","error"));
		}
	}
	
	
//	@PostMapping("/admin/bookChk")
//	@ResponseBody
//	public ResponseEntity<?> bookChk(@RequestBody Books book, @RequestParam String msg, @RequestParam String bookCategoryName){
//		Category category = categoryRepository.findByBookCategoryName(bookCategoryName)
//												.orElseThrow(()->new RuntimeException("카테고리를 찾을 수 없습니다."+book.getCategory().getBookCategoryName()));
//		book.setCategory(category);
//		System.out.println("getBookAuthor():"+book.getBookAuthor());
//		System.out.println("bookCategoryName:"+bookCategoryName);
//		if(msg.equals("exist")) {
//			//booksRepository.save(book);
//		}
//		
//		return ResponseEntity.ok(Map.of("status","success"));
//	}
	//--------------------------------------------
	
	@GetMapping(value="/admin/memberDelete/{memberId}")
	public String memberDelete(@PathVariable String memberId){ 
		System.out.println("memberId="+memberId);
		adminRepository.deleteByMemberId(memberId);
		return "redirect:/admin/membersManage";
	}
	@GetMapping(value="/admin/gptHashTag",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String gptHashTag(@RequestBody String content) {
//		 return chatGPTService.getAPIReponse(content);
		 return "";
	}
}
