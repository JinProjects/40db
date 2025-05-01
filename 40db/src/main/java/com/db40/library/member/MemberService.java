package com.db40.library.member;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Service  //##1
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository   memberRepository;
	private final PasswordEncoder    passwordEncoder;  // SecurityConfig
	private final MemberStatusRepository memberStatusRepository;
	private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
	private static final String mNUM_REGEX = "^01[016789]\\d{3,4}\\d{4}$";
	private static final String dNAME_REGEX = "^[가-힣a-zA-Z0-9]{2,20}$";
	
	// 이메일 정규식 T/F
	public boolean isValidEmail(String email) {
	    return email != null && email.matches(EMAIL_REGEX);
	}
	// 휴대전화 번호 정규식 T/F
	public boolean isValidmNUM(String mNUM) {
		return mNUM != null && mNUM.matches(mNUM_REGEX);
	}
	// 별명 정규식 T/F
	public boolean isValiddNAME(String dNAME) {
		return dNAME != null && dNAME.matches(dNAME_REGEX);
	}
	
	//insert
	public Member insertMember(Member member) {
		try {
			member.setJoinIp(InetAddress.getLocalHost().getHostAddress());
			member.setMemberStatus(memberStatusRepository.findById(1).get());
			member.setMemberPass(passwordEncoder.encode( member.getMemberPass()  ));
		} catch (UnknownHostException e) { e.printStackTrace();}
		return memberRepository.save(member);
	}
	/* 계정 찾기 */
	/* 계정 찾기 */
	// 실명과 휴대폰 번호로 id찾기
	public Long forFindId(String name, String mobile) {
		Long findid = memberRepository.findIdByRealNameAndMobile(name, mobile);
		return findid;
	}
	// 아이디 실명 휴대폰 번호로 id찾기
	public Long forFindPass(String id, String name, String mobile) {
		Long findid = memberRepository.findIdByMemberIdNameAndMobile(id, name, mobile);
		return findid;
	}
	// id로 비밀번호 변경하기
	public void updatePass(Long id, String pass) {
		pass = passwordEncoder.encode(pass);
		memberRepository.updatePasswordById(id, pass);
	}
	// memberID로 id 찾기
	public Long findIDByMemberId(String memberId) {
		Long id = memberRepository.findIdByMemberId(memberId);
		return id;
	}
	/* 계정 찾기 */
	
	/* 마이페이지 */
	/* 마이페이지 */
	// 비밀번호 변경
	@Transactional
	public void updatePasswordInMypage(String newpassword, Long id, String oldpassword, RedirectAttributes redirectAttributes) {
	    Optional<Member> find = memberRepository.findById(id);
	    if (find.isEmpty()) { redirectAttributes.addFlashAttribute("fail", "정상적이지 않은 접근입니다"); return; }
	    Member member = find.get();
	    // 비밀번호 검증
	    if (!passwordEncoder.matches(oldpassword, member.getMemberPass())) {
	    	redirectAttributes.addFlashAttribute("fail", "비밀번호를 다시 확인해주세요"); 
	    	return;}

	    String encodedNewPassword = passwordEncoder.encode(newpassword);
	    member.setMemberPass(encodedNewPassword);
	    memberRepository.save(member);
	    redirectAttributes.addFlashAttribute("success", "비밀번호가 변경되었습니다");
	}
	
	// 이메일 변경
	@Transactional
	public void updateEmailInMypage(Long id, String memberPass, String newemail, RedirectAttributes redirectAttributes) {
		Optional<Member> find = memberRepository.findById(id);
		if (find.isEmpty()) { redirectAttributes.addFlashAttribute("fail", "정상적이지 않은 접근입니다"); return; }
	    Member member = find.get();
	    // 이메일 양식 검사
	    if (!isValidEmail(newemail)) {
	        redirectAttributes.addFlashAttribute("fail", "이메일 형식이 올바르지 않습니다"); return; }
	    // 이메일 중복 검사
	    if(memberRepository.duplicateEmail(newemail)) {
	    	redirectAttributes.addFlashAttribute("fail", "이미 사용중인 이메일입니다"); return; }
	    // 비밀번호 검증
	    if(!passwordEncoder.matches(memberPass, member.getMemberPass())) {
	    	redirectAttributes.addFlashAttribute("fail", "비밀번호가 일치하지 않습니다"); return; }
	    
	    member.setEmail(newemail);
	    memberRepository.save(member);
	    redirectAttributes.addFlashAttribute("success", "이메일이 변경되었습니다");
	}
	// 주소 변경
	@Transactional
	public void updateAddressInMypage(Long id, String adressPost, String adressRoad, String adressJibun, String adressDetail, RedirectAttributes redirectAttributes) {
		Optional<Member> find = memberRepository.findById(id);
		if (find.isEmpty()) { redirectAttributes.addFlashAttribute("fail", "정상적이지 않은 접근입니다"); return; }
	    Member member = find.get();
	    
	    member.setAddressPost(adressPost);
	    member.setAddressRoad(adressRoad);
	    member.setAddressJibun(adressJibun);
	    member.setAddressDetail(adressDetail);
	    memberRepository.save(member);
	    redirectAttributes.addFlashAttribute("success", "주소가 변경되었습니다");
	}
	// 휴대전화 번호 변경
	@Transactional
	public void updateMobileNumberInMypage(Long id, String mobileNumber, String memberPass, RedirectAttributes redirectAttributes) {
		Optional<Member> find = memberRepository.findById(id);
		if (find.isEmpty()) { redirectAttributes.addFlashAttribute("fail", "정상적이지 않은 접근입니다"); return; }
	    Member member = find.get();
	    // 휴대전화 번호 검증
	    if(!isValidmNUM(mobileNumber)) {
	    	redirectAttributes.addFlashAttribute("fail", "휴대전화 번호를 다시 확인해주세요"); return; }
	    // 비밀번호 검증
	    if(!passwordEncoder.matches(memberPass, member.getMemberPass())) {
	    	redirectAttributes.addFlashAttribute("fail", "비밀번호가 일치하지 않습니다"); return; }
	    
	    member.setMobileNumber(mobileNumber);
	    memberRepository.save(member);
	    redirectAttributes.addFlashAttribute("success", "휴대전화 번호가 변경되었습니다");
	}
	
	// 별명 변경
	@Transactional
	public void updateDisplayNameInMypage(Long id, String displayName, RedirectAttributes redirectAttributes) {
		Optional<Member> find = memberRepository.findById(id);
		if (find.isEmpty()) { redirectAttributes.addFlashAttribute("fail", "정상적이지 않은 접근입니다"); return; }
	    Member member = find.get();
	    
	    if(!isValiddNAME(displayName)) {
	    	redirectAttributes.addFlashAttribute("fail", "사용하실 별명을 다시 확인해주세요"); return; }
	    
	    member.setDisplayName(displayName);
	    memberRepository.save(member);
	    redirectAttributes.addFlashAttribute("success", "별명이 변경되었습니다");
	}
	// 회원탈퇴
	@Transactional
	public int deleteAccountInMypage(Long id, String memberId, String memberPass, RedirectAttributes redirectAttributes) {
		Optional<Member> find = memberRepository.findById(id);
		if (find.isEmpty()) { redirectAttributes.addFlashAttribute("fail", "정상적이지 않은 접근입니다"); return 0; }
	    Member member = find.get();
	    System.out.println("find : "+find);
	    // 아이디 검증
	    if(!memberId.equals(member.getMemberId())) {
	    	redirectAttributes.addFlashAttribute("fail", "아이디 또는 비밀번호가 일치하지 않습니다"); return 0; }
	    // 비밀번호 검증
	    if(!passwordEncoder.matches(memberPass, member.getMemberPass())) {
	    	redirectAttributes.addFlashAttribute("fail", "아이디 또는 비밀번호가 일치하지 않습니다"); return 0; }
	    
	    memberRepository.delete(member);
	    redirectAttributes.addFlashAttribute("deleteAccount", "회원탈퇴가 정상 처리되었습니다. 이용해주셔서 감사합니다");
	    return 1;
	}
	/* 마이페이지 */
	
	//selectAll
	public List<Member> selectMemberAll(){  
		return memberRepository.findAll();
	}
	
	//select
	public Member selectMember(Long id) {  
		return memberRepository.findById(id).get();
	}
	 
	// 중복체크
	// 아이디
	public Member selectUserMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).get();
	}

	/* ajax - 데이터 가져오기 */
	/* ajax - 데이터 가져오기 */
	// 이메일
	public Member selectUserEmail(String email) {
		return memberRepository.findByEmail(email).get(); }
	// displayName 가져오기 
	public String selectdisplayNameByMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).map(Member::getDisplayName).orElse(""); }
	// Email 가져오기 
	public String selectEmailByMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).map(Member::getEmail).orElse(""); }
	// address 가져오기 
	public String selectAddressPostByMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).map(Member::getAddressPost).orElse(""); }
	public String selectAddressRoadByMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).map(Member::getAddressRoad).orElse(""); }
	public String selectAddressJibunByMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).map(Member::getAddressJibun).orElse(""); }
	public String selectAddressDetailByMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).map(Member::getAddressDetail).orElse(""); }
	// MobileNumber 가져오기
	public String selectMobileNumberByMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).map(Member::getMobileNumber).orElse(""); }
	// 실명
	public String selectRealNameByMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId).map(Member::getRealName).orElse(""); }
	// 생년월일
	public String selectBirthDateByMemberId(String memberId) {
		LocalDate date =  memberRepository.findByMemberId(memberId).map(Member::getBirthDate).orElseThrow();
		return date.toString();}
	// 성별
	public String selectGenderByMemberId(String memberId) {
		char gender =  memberRepository.findByMemberId(memberId).map(Member::getGender).orElseThrow();
		if (gender=='F') { return "여성"; }
		if (gender=='M') { return "남성"; }
		return "??";
	}
	// 가입일
	public String selectJoinDateByMemberId(String memberId) {
		LocalDate date =  memberRepository.findByMemberId(memberId).map(Member::getMemberJoinDate).orElseThrow();
		return date.toString();}
}
