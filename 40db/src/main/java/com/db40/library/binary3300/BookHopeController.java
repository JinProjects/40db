package com.db40.library.binary3300;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookHopeController {
	private final BookHopeService bookHopeService;
	@Autowired BookHopeApi bookHopeApi;
	@Autowired BookHopeRepository bookHopeRepository;
	
	
	// 희망도서 검색
	@GetMapping("/hopeBook/hope_search")
	public String hope_now(Model model) {
		return "/hopeBook/hope_search";
	} // http://localhost:8080/hopeBook/hope_now

//	// 희망도서 작성하기
//	@GetMapping("/hopeBook/hope_insert")
//	public String insert_get() {
//		return "/hopeBook/write";
//	} // http://localhost:8080/bookhope/hope_insert
//
//	@PostMapping("/hopeBook/hope_insert")
//	public String insert_post(BookHope bookhope) {
//		return "redirect:/hopeBook/hope_now";
//	} // http://localhost:8080/bookhope/hope_insert

	
//	// 희망조서 내용 자세히 보기 {book_hope_no}  	@PathVariable Long id, Model model	 model.addAttribute("dto",bookHopeService.find(id) ); 
//	@GetMapping("/hopeBook/hope_detail") 
//	public String detail(Model model) {
//		 return "/hopeBook/hope_detail"; 
//	}
	
	// 검색시 타이틀로 불러옴
	@GetMapping(value="/hopeBook/hope_usersearch/{search}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String hope_usersearch( @PathVariable String search) throws IOException {
		//System.out.println("실행");
		return  bookHopeApi.getApi(search);
	}
	
	/*
		//희망도서 신청 
	@PostMapping("/hopeBook/submit")
    public String submitHopeBook(        
    		@RequestParam String title,
            @RequestParam String author,
            @RequestParam String publisher,
            @RequestParam String isbn13,
            @RequestParam String reason,  RedirectAttributes rttr ) {
        BookHope hopeBook = new BookHope();
        hopeBook.setBook_title(title);
        hopeBook.setBook_auther(author);
        hopeBook.setBook_publisher(publisher);
        hopeBook.setBook_isbn(isbn13);
        hopeBook.setBook_reason(reason);
        String msg = "fail";
        if(bookHopeService.saveBookHope(hopeBook) != null) {  msg = "희망 도서 신청이 완료되었습니다.";}
		rttr.addFlashAttribute("msg", msg);
		return "redirect:/hopeBook/list"; 
    	
    }   */
	
	//전체 리스트 가져오기+페이징. 망할까봐 밑에 백업
	@GetMapping("/hopeBook/hope_list")
	public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page ){
		//model.addAttribute("list" , bookHopeService.findAllByOrderByDesc());  //##전체리스트뽑고
		model.addAttribute("list",bookHopeService.getPaging(page).getContent()); 
		model.addAttribute("paging",new PagingDto(bookHopeService.findAll().size(),page)); 
		

//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	      UserDetails userDetails = (UserDetails)authentication.getPrincipal();
//	      String name = userDetails.getUsername();
//	      if(name.equals("admin")) {
//	          model.addAttribute("layoutDeco", "fragments/admin/adminLayout");
//	       }else {
//	          model.addAttribute("layoutDeco", "fragments/layout");
//	       }
		String name = "";
		String layout = "fragments/layout";
	      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	      System.out.println("authentication.isAuthenticated()="+authentication.isAuthenticated());
	     
	      if(authentication != null && authentication.isAuthenticated()) {
	    	  
	         Object principal =  authentication.getPrincipal();
	         if(principal instanceof UserDetails) {
	        	name = ((UserDetails) principal).getUsername(); 
	         }else {
	        	 name = principal.toString();
	         }
	         
	         Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();
	         boolean isAdmin = authorites.stream().anyMatch(grantedAuthority -> 
	         grantedAuthority.getAuthority().equals("ROLE_ADMIN")
	        		 );
	        		          
	         if(isAdmin) {
	        	layout = "fragments/admin/adminLayout";
	            model.addAttribute("layoutDeco", layout);
	         }else {
	            model.addAttribute("layoutDeco", layout);
	         }
	      }
	      if(name != null){
	    	  System.out.println(name);
	         model.addAttribute("layoutDeco", layout);
	      }
	      model.addAttribute("active", "hope_list");
		return "hopeBook/hope_list"; 
	}
	
	
	//1. 전체 리스트 읽어오기 
//	@GetMapping("/hopeBook/hope_list")
//	public String list(Model model){
//		model.addAttribute("list" , bookHopeService.findAllByOrderByDesc());  //##전체리스트뽑고
//		return "hopeBook/hope_list"; 
//	} 
	 
	//2. 사용자, 희망도서를 api로 받은 정보를 write로 보내는 기능
		@PostMapping("/hopeBook/hope_write")
		public String bookForm(@RequestParam String title, 
		                       @RequestParam String author, 
		                       @RequestParam String publisher, 
		                       @RequestParam String isbn13, 
		                       @RequestParam String pubDate, 
		                       @RequestParam String priceStandard, 
		                       @RequestParam String categoryName, 
		                       @RequestParam boolean adult, 
		                       Model model) {
		    model.addAttribute("title", title);
		    model.addAttribute("author", author);
		    model.addAttribute("publisher", publisher);
		    model.addAttribute("isbn13", isbn13);
		    model.addAttribute("pubDate", pubDate);
		    model.addAttribute("priceStandard", priceStandard);
		    model.addAttribute("categoryName", categoryName);
		    model.addAttribute("adult", adult);
		    
		    return "hopeBook/hope_write"; // 폼 페이지 이름
		}
		
		
	//3. 글 쓰고 희망도서 신청 
	@PostMapping("/hopeBook/hope_insert")
    public String submitHopeBook(BookHope bookHope,  RedirectAttributes rttr ) {
		System.out.println(".........."+bookHope);
        String msg = "fail";
		System.out.println(".........."+bookHopeService.saveBookHope(bookHope));
        if(bookHopeService.saveBookHope(bookHope) != null) {  msg = "희망 도서 신청이 완료되었습니다.";}
		rttr.addFlashAttribute("msg", msg);
		return "redirect:/hopeBook/hope_list";  
    } 
	
	//관리자, 희망도서 디테일 확인 페이지
	@GetMapping("/hopeBook/hopeAdminDetail/{id}")
	public String hopeAdminDetail(@PathVariable Long id, Model model) {
		model.addAttribute("dto",bookHopeService.find(id) );
		return "/hopeBook/hope_admin_detail";
	}
	 
	// 관리자, 희망도서 반려하기/등록하기 업데이트에서 하나로 될거같음
	@PostMapping("/hopeBook/hopeAdminUpdate")
    public String hopeAdminUpdate(@RequestParam("bookHopeNo") Long bookHopeNo,
            					  @RequestParam("book_hope_stat") String status) {
		bookHopeService.hopeAdminUpdate(bookHopeNo, status);
		
		return "redirect:/hopeBook/hope_list";
    }
	
//    @GetMapping("/hopeBook/hopeAdminUpdate")
//    public String showAdminDetailPage(HttpServletRequest request , Model model) {
//    	String hopeStatUpdate =  request.getParameter("hopeStatUpdate");
//    	//String hopeStatUpdate =  (String)request.getAttribute("hopeStatUpdate");
//    	System.out.println("hopeStatUpdate="+ hopeStatUpdate);
//       // model.addAttribute("bookHope", bookHope);
//        return "redirect:/hopeBook/hope_list";
//    }
	

}
