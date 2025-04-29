package com.db40.library.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//public class MemberRole{
@Getter
public enum MemberRole{
ADMIN("ROLE_ADMIN") , MEMBER("ROLE_MEMBER");
private String value;

private MemberRole(String value) { this.value = value; }
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//	@Column(nullable = false, unique = true)
//	private String roles;
}
