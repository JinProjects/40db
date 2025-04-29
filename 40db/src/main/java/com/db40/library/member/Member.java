package com.db40.library.member;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity  @Getter  @Setter
public class Member {
	// DB id
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 계정 정보
	@Column(unique=true, name="member_id", nullable=false) private String memberId; // 아이디
	@Column(name="member_pass", nullable=false) private String memberPass; 			// 비밀번호
	
	// 사용자 정보
	@Column(name="member_email", unique=true, nullable=false) private String email; 	// 이메일
	@Column(name="member_real_name", nullable=false) private String realName;			// 실명
	@Column(name="member_display_name", nullable=false)private String displayName;		// 표시되는 이름
	@Column(name="member_mobile_number", nullable=false)private String mobileNumber;	// 휴대전화
	@Column(name="member_birth_date", nullable=false)private LocalDate birthDate;		// 생년월일
	@Column(name="member_gender", nullable=false)private char gender;					// 성별
	
	// 사용자 실주소
	@Column(name="address_post", nullable=false)	private String addressPost;		// 우편번호
	@Column(name="address_road", nullable=false)	private String addressRoad; 	// 도로명
	@Column(name="address_jibun", nullable=false)	private String addressJibun;	// 지번
	@Column(name="address_detail", nullable=false)	private String addressDetail;	// 상세
	
	// 자동 입력
	@Column(name="member_role", nullable=false)private String memberRole = "MEMBER";					// 회원구분:멤버
	@Column(name="member_warning", nullable=false)private Integer memberWarning = 0;					// 회원경고:0
	@Column(name="member_join_date", nullable=false)private LocalDate memberJoinDate = LocalDate.now();	// 생성일
	@Column(name="member_join_ip", nullable=false)private String joinIp;								// 
	
	// 계정 상태
	@ManyToOne @JoinColumn(name="member_status", nullable=false) private MemberStatus memberStatus;
	
}