package com.db40.library.member;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MemberController {
	
	@Autowired MemberRepository mR;
	@Autowired MemberService service;
	@Autowired KaKaoLogin kakao;
	@Autowired NaverLogin naver;
	@Autowired GoogleLogin google;

	/* 기본 회원가입 페이지 */
	@GetMapping("/member/join")
	public String join(HttpServletRequest request, Principal principal, MemberForm memberForm) {
		if ( principal != null) {
			return "member/mypage/recentBorrow";
		}
		return "member/join"; }
	
	/* 기본 회원가입 기능 */
	@PostMapping("/member/join")
	public String join(@Valid MemberForm memberForm, BindingResult bindingResult) {  
		
		if(bindingResult.hasErrors()) { return "member/join"; }
		if( !memberForm.getMemberPass().equals(  memberForm.getPassword2()) ) {
			//bindingResult.rejectValue(필드명 , 오류코드, 에러메시지)
			bindingResult.rejectValue("password2", "pawordInCorrect","패스워드를 확인해주세요");
			return "member/join"; 
		} 
		
		try {
			Member  member = new Member();
			member.setMemberId(memberForm.getMemberId());
			member.setMemberPass(memberForm.getMemberPass());
			member.setEmail(memberForm.getEmail());
			member.setBirthDate(memberForm.getBirthDate());
			member.setAddressPost(memberForm.getAddressPost());
			member.setAddressJibun(memberForm.getAddressJibun());
			member.setAddressRoad(memberForm.getAddressRoad());
			member.setAddressDetail(memberForm.getAddressDetail());
			member.setDisplayName(memberForm.getDisplayName());
			member.setMobileNumber(memberForm.getMobileNumber());
			member.setGender(memberForm.getGender());
			member.setRealName(memberForm.getRealName());
			service.insertMember(member);
		}catch(DataIntegrityViolationException e) { // 무결성- 중복키, 외래키제약, 데이터형식불일치
			e.printStackTrace();
			bindingResult.reject("failed" , "입력 혹은 선택하지 않은 항목이 존재합니다");
			return "member/join"; 
		} catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("failed" , e.getMessage());
			return "member/join"; 
		}
		return "member/login"; 
	}
	
	/* 통합 회원가입 ( 일반, 카카오, 네이버 ) */
	/* 통합 회원가입 ( 일반, 카카오, 네이버 ) */
	// 통합 로그인 / 회원가입 / 로그인 된 유저일 경우 마이페이지로 이동
	@GetMapping("/member/login") 
		public String identify(HttpServletRequest request, Principal principal, Model model) {
			if(principal != null) {
				return "member/mypage/recentBorrow";
			}
			model.addAttribute("kakao", kakao.identify());	// 카카오
			model.addAttribute("naver", naver.identify());	// 네이버
			model.addAttribute("google", google.identify()); // 구글
			return "member/login";
		}
	
	// localhot:8080/kakao 사용자 정보 가져오기 - 카카오
	@GetMapping("/kakao")
	public String  kakaouser(@RequestParam("code") String code, Model model) {
		List<String>  infos = kakao.identified(code);
		// 성별 타입 변환
		String gend = infos.get(3).toUpperCase();
		char gendchar = gend.charAt(0);
		// 휴대전화번호 타입 변환
		String mobile = infos.get(4);
		String formatmobile = mobile.replace("+82", "0").replaceAll("[^0-9]", "");
		// 생년월일 타입 변환
		String birthdate = infos.get(6)+infos.get(5);
		DateTimeFormatter inputbirth = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate inputbirthdate = LocalDate.parse(birthdate, inputbirth);
		DateTimeFormatter outputbirth = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formatbirth = inputbirthdate.format(outputbirth);
		
		MemberForm memberForm = new MemberForm();
		memberForm.setDisplayName(infos.get(0));
		memberForm.setEmail(infos.get(1));
		memberForm.setRealName(infos.get(2));
		memberForm.setGender(gendchar);
		memberForm.setMobileNumber(formatmobile);
		memberForm.setBirthDate(LocalDate.parse(formatbirth));
		model.addAttribute("memberForm", memberForm);
		kakao.unlink();
		return "member/join";
	}
	
	@GetMapping("/naver")
	public String naveruser(@RequestParam("code") String code, String state, Model model) {
		List<String> infos = naver.identified(code, state);
		// 성별 타입 변환
		char genderchar = infos.get(3).charAt(0);
		// 생년월일 변환
		String birthdate = infos.get(5)+(infos.get(4).replaceAll("[^0-9]", ""));
		DateTimeFormatter inputbirth = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate inputbirthdate = LocalDate.parse(birthdate, inputbirth);
		DateTimeFormatter outputbirth = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formatbirth = inputbirthdate.format(outputbirth);
		// 휴대전화번호 변환
		String mobile = infos.get(6).replaceAll("[^0-9]", "");
		
		MemberForm memberForm = new MemberForm();
		memberForm.setRealName(infos.get(0));
		memberForm.setEmail(infos.get(1));
		memberForm.setDisplayName(infos.get(2));
		memberForm.setGender(genderchar);
		memberForm.setBirthDate(LocalDate.parse(formatbirth));
		memberForm.setMobileNumber(mobile);
		
		model.addAttribute("memberForm", memberForm);
		naver.unlink();
		return "member/join";
	}
	@GetMapping("/google")
	public String googleuser(@RequestParam("code") String code, Model model) {
		List<String> infos = google.identified(code);
		
		MemberForm memberForm = new MemberForm();
		memberForm.setEmail(infos.get(0));
		model.addAttribute("memberForm", memberForm);
		
		return "member/join";
	}
	
	/* 통합 회원가입 ( 일반, 카카오, 네이버 ) */	

	/* 아이디 찾기 */
	/* 아이디 찾기 */
	/* 아이디 찾기 페이지 */
	@GetMapping("/member/findid")
	public String findid() { return "member/findid";}
	
	@PostMapping("/member/findid")
	public String findid(String realName, String mobileNumber, Model model) {
		Long find = service.forFindId(realName, mobileNumber);
		Member member = new Member();
		
		if (find==null) {
			model.addAttribute("error", "계정이 존재하지 않습니다");
			return "member/findid";
		}
		member = service.selectMember((long)find);
		model.addAttribute("found", member.getMemberId());
		model.addAttribute("realName", member.getRealName());
		return "member/foundid";
	}
	
	@GetMapping("/member/foundid")
	public String foundid() { return "member/foundid";}
	/* 아이디 찾기 */
	/* 아이디 찾기 */
		
	
	/* 비밀번호 찾기 */
	/* 비밀번호 찾기 */
	@GetMapping("/member/findpw")
	public String findpw() { return "member/findpw"; }
	
	@PostMapping("/member/findpw")
	public String findpw(String memberId, String realName, String mobileNumber, Model model) {
		Long findpw = service.forFindPass(memberId, realName, mobileNumber);
		System.out.println(findpw);
		if (findpw == null) {
			model.addAttribute("error", "일치하는 계정 없음");
			return "member/findpw";
		}
		model.addAttribute("success", findpw);
		return "member/foundpw";
		}
	
	@GetMapping("/member/foundpw")
	public String foundpw() { return "member/foundpw";}
	
	@PostMapping("/member/foundpw")
	public String foundpw(Long id, String memberPass) { 
		service.updatePass(id, memberPass);
		return "member/login";
	}
	/* 비밀번호 찾기 */
	/* 비밀번호 찾기 */
	
	
	/* 마이페이지 */
	/* 마이페이지 */
	@GetMapping("/member/mypage/main")
	public String mypageMain() {  return "member/mypage/recentBorrow"; }
	
	@GetMapping("/member/mypage/upass")
	public String mypageUpdatePassword() {
		 return "member/mypage/updatePassword";
	}
	
	@PostMapping("/member/mypage/upass")
	public String mypageUpdatePassword_post(String memberId, String oldpassword, String newpassword, String newpassword2, RedirectAttributes redirectAttributes) {
		if (!newpassword.equals(newpassword2)) {
			redirectAttributes.addFlashAttribute("fail", "새 비밀번호가 일치하지 않습니다");
			return "redirect:/member/mypage/upass";
		}
		Long id = service.selectUserMemberId(memberId).getId();
		service.updatePasswordInMypage(newpassword, id, oldpassword, redirectAttributes);
		return "redirect:/member/mypage/upass";
	}
	
	@GetMapping("/member/mypage/uemail")
	public String mypageUpdateEmail() { return "member/mypage/updateEmail"; }
	
	@PostMapping("/member/mypage/uemail")
	public String mypageUpdateEmail_post(String memberId, String memberPass, String newemail, RedirectAttributes redirectAttributes) {
		Long id = service.selectUserMemberId(memberId).getId();
		service.updateEmailInMypage(id, memberPass, newemail, redirectAttributes);
		return "redirect:/member/mypage/uemail";
	}
	
	@GetMapping("/member/mypage/uaddress")
	public String mypageUpdateAddress(String memberId, Model model) {
		return "member/mypage/updateAddress"; }
	
	@PostMapping("/member/mypage/uaddress")
	public String mypageUpdateAddress_post(String memberId, String addressPost, String addressRoad, String addressJibun, String addressDetail, RedirectAttributes redirectAttributes) {
		Long id = service.selectUserMemberId(memberId).getId();
		service.updateAddressInMypage(id, addressPost, addressRoad, addressJibun, addressDetail, redirectAttributes);
		return "redirect:/member/mypage/uaddress";
	}
	
	@GetMapping("/member/mypage/umobile")
	public String mypageUpdateMobile() { return "member/mypage/updateMobileNumber"; }
	
	@PostMapping("/member/mypage/umobile")
	public String mypageUpdateMobile_post(String memberId2, String newMobileNumber, String memberPass, RedirectAttributes redirectAttributes) {
		Long id = service.selectUserMemberId(memberId2).getId();
		service.updateMobileNumberInMypage(id, newMobileNumber, memberPass, redirectAttributes);
		return "redirect:/member/mypage/umobile";
	}
	
	@GetMapping("member/mypage/udname")
	public String mypageUpdatedName() { return "member/mypage/updateDisplayName"; }
	
	@PostMapping("member/mypage/udname")
	public String mypageUpdatdName_post(String memberId2, String newdisplayName, RedirectAttributes redirectAttributes) {
		Long id = service.selectUserMemberId(memberId2).getId();
		service.updateDisplayNameInMypage(id, newdisplayName, redirectAttributes);
		return "redirect:/member/mypage/udname";
	}
	@GetMapping("member/mypage/deleteAccount")
	public String deleteAccount() { return "member/mypage/deleteAccount"; }
	
	@PostMapping("member/mypage/deleteAccount")
	public String deleteAccount(String memberId2, String realName, String memberPass, HttpServletRequest request, HttpServletResponse response) {
	    Long id = service.findIDByMemberId(memberId2);
	    service.deleteAccountInMypage(id, realName, memberPass);

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null) {
	        new SecurityContextLogoutHandler().logout(request, response, authentication);
	    }
	    return "redirect:/member/login";
	}
	/* 마이페이지 */
	
	
	// 중복체크
	// 아이디
	@ResponseBody
	@GetMapping("/member/join/reduplicationMId/{memberId}")
	public Map<String, Object> reduplicationMIdCheck(@PathVariable String memberId) {
	    Map<String, Object> resultMId = new HashMap<>();
	    
	    boolean exists = mR.findByMemberId(memberId).isPresent(); // true = 이미 존재

	    resultMId.put("resultMId", exists ? "사용불가" : "사용가능");
	    return resultMId;
	}
	// 이메일
	@ResponseBody
	@GetMapping("/member/join/reduplicationEmail/{email}")
	public Map<String, Object> reduplicationEmailCheck(@PathVariable String email) {
	    Map<String, Object> resultEmail = new HashMap<>();
	    
	    boolean exists = mR.findByEmail(email).isPresent();

	    resultEmail.put("resultEmail", exists ? "사용불가" : "사용가능");
	    return resultEmail;
	}
	
	/* ajax - 데이터 가져오기 */
	/* ajax - 데이터 가져오기 */
	
	// memberid로 displayName 가져오기
	@GetMapping("/getDisplayNameByMemberId") @ResponseBody
	public String getDisplayNameByMemberId(@RequestParam String memberId) {
	    return service.selectdisplayNameByMemberId(memberId);
	}
	// memberid로 email 가져오기
	@GetMapping("/getEmailByMemberId") @ResponseBody
	public String getEmailByMemberId(@RequestParam String memberId) {
		return service.selectEmailByMemberId(memberId);
	}
	// memberid로 address* 가져오기
	@GetMapping("/getAddressPostByMemberId") @ResponseBody
	public String getAddressPostByMemberId(@RequestParam String memberId) {
		return service.selectAddressPostByMemberId(memberId); }
	@GetMapping("/getAddressRoadByMemberId") @ResponseBody
	public String getAddressRoadByMemberId(@RequestParam String memberId) {
		return service.selectAddressRoadByMemberId(memberId); }
	@GetMapping("/getAddressJibunByMemberId") @ResponseBody
	public String getAddressJinunByMemberId(@RequestParam String memberId) {
		return service.selectAddressJibunByMemberId(memberId); }
	@GetMapping("/getAddressDetailByMemberId") @ResponseBody
	public String getAddressDetailByMemberId(@RequestParam String memberId) {
		return service.selectAddressDetailByMemberId(memberId); }
	// mobileNumber 가져오기
	@GetMapping("/getMobileNumberByMemberId") @ResponseBody
	public  String getMobileNumberByMemberId(@RequestParam String memberId) {
		return service.selectMobileNumberByMemberId(memberId);
	}
	
	
}
