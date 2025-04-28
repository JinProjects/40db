package com.db40.library.member;
 
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter  @Setter
public class MemberForm {
	
	@NotEmpty(message="아이디를 입력하지 않았습니다")
	@Size(min=4, max=20, message="아이디는 최소 4자, 최대 20자여야 합니다")
	@Pattern( regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{4,20}$",
		      message = "아이디는 영문자와 숫자 조합으로 4~20자 이내여야 하며, 영문자는 반드시 포함되어야 합니다." )
	private String memberId;

	@NotEmpty(message="비밀번호를 입력하지 않았습니다")
	private String memberPass;

	@NotEmpty(message="비밀번호가 일치하지 않습니다")
	private String password2; 
	
	@NotEmpty(message="실명을 입력하지 않았습니다")
	private String realName;
	
	@NotEmpty(message="휴대폰 번호를 입력하지 않았습니다")
	@Pattern( regexp = "^[0-9]{11}$",
		      message = "전화번호는 11자리 숫자만 입력 가능합니다." )
	private String mobileNumber;
	
	@NotEmpty(message="이메일을 입력하지 않았습니다")
	@Pattern( regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
		      message = "올바른 이메일 주소 형식이 아닙니다." )
	private String email;
	
	@NotNull(message="성별을 선택하지 않았습니다")
	private char gender;
	
	@NotNull(message="우편번호를 입력하지 않았습니다")
	private String addressPost;
	
	private String addressJibun;
	
	private String addressRoad;
	
	@NotEmpty(message="상세주소를 입력하지 않았습니다")
	private String addressDetail;
	
	@NotNull(message="생년월일을 입력하지 않았습니다")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	@NotEmpty(message="별명을 입력하지 않았습니다")
	@Pattern( regexp = "^[가-힣a-zA-Z0-9]{2,20}$",
		      message = "별명은 한글, 영문자, 숫자만 가능하며 2자 이상 32자 이하로 입력해주세요." )
	private String displayName;
}

//https://beanvalidation.org/