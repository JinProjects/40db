package com.db40.library.member;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
public class MemberStatus {
	
	@Id private Integer id;
	
	@Column(name="status", nullable=false)private String status;
	
	@OneToMany(mappedBy = "memberStatus")
	List<Member> memberList = new ArrayList<>();
}
