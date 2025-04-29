package com.db40.library.binary3300;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.db40.library.member.Member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity @Getter @Setter   @ToString(exclude = "member")
public class BookHope {
	@Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_hope_no")
	private Long bookHopeNo;

	
	private String book_title;
	
	private String book_isbn;
	
	private String book_auther;
	
	private String book_publisher; //출판사 
	
	private String book_published_date; //출판일 
	
	@Column(columnDefinition = "text")
	private String book_reason;
	
	private String book_hope_stat;
	
	@Column(updatable = false)
	private LocalDateTime createDate = LocalDateTime.now();
	
	private Integer bookPriceStandard;
	
	private String bookCategoryName;
	
	private Boolean bookAdult;
	
	
	//@OneToMany(mappedBy = "bookHope")
	//private Member member;
	
	@ManyToOne
	@JoinColumn(name = "member_id") // 외래 키 설정
	private Member member; // 단일 Member 객체

	
}
