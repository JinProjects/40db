package com.db40.library.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.db40.library.sh.Borrow;

public interface MemberRepository  extends JpaRepository<Member, Long>{
	
	@Query("select m from Member m where m.id=:id")
	Optional<Member> findById(Long id);
	
	@Modifying  
	@Transactional  
	@Query("update Member m set m.memberPass=:password where m.memberPass=:old  and m.id=:id")
	int updateByIdAndPassword(String password, String old, Long id);
	
	/* 사용자의 본명과 휴대폰 번호로 id찾기 */
	 @Query("select m.id from Member m where m.realName=:name and m.mobileNumber=:mobile")
	 Long findIdByRealNameAndMobile(String name, String mobile);
	 
	/* 사용자의 아이디와 본명, 휴대폰 번호로 id찾기 */
	 @Query("select m.id from Member m where m.memberId=:id and m.realName=:name and m.mobileNumber=:mobile")
	 Long findIdByMemberIdNameAndMobile(String id, String name, String mobile);
		 
	@Modifying @Transactional
	@Query("update Member m set m.memberPass=:pass where m.id=:id")
	void updatePasswordById(Long id, String pass);
	
	/* api를 활용한 이메일 중복 시 바로 로그인 */
	@Query("select m from Member m where m.email=:email")
	int finyIdbyEmail(String email);
	
	@Query("select m from Member m where m.memberId=:memberId")
	Optional<Member> findByMemberId(String memberId);	
	
	@Query("select m from Member m where m.email=:email")
	Optional<Member> findByEmail(String email);	
	
	@Query("select m.id from Member m where m.memberId=:memberId")
	Long findIdByMemberId(String memberId);
	
	@Modifying @Transactional @Query("update Member m set m.memberPass=:newpassword where m.id=:id and m.memberPass=:oldpassword")
	void updatePasswordInMypage(String newpassword, Long id, String oldpassword);
	
	// 마이페이지 - memberId로 주소 변경하기
	@Modifying @Transactional
	@Query("update Member m set m.addressPost=:aP, m.addressRoad=:aR, m.addressJibun=:aJ, m.addressDetail=:aD where m.memberId=:mI")
	void updateAddressInMypage(String mI, String aP, String aR, String aJ, String aD);
	
	// 휴대전화번호
	@Modifying @Transactional
	@Query("update Member m set m.mobileNumber=:mN where m.memberId=:mI")
	void updateMobileNumberInMypage(String mI, String mN);
	
	// 별명
	@Modifying @Transactional
	@Query("update Member m set m.displayName=:dN where m.memberId=:mI")
	void updateDisplayNameInMypage(String mI, String dN);
	
	// 이메일 중복 검사
	@Query("select case when count(m) > 0 then true else false end from Member m where m.email=:email")
	boolean duplicateEmail(String email);
	
}







