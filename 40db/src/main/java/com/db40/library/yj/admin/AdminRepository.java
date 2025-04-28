package com.db40.library.yj.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.db40.library.member.Member;

public interface AdminRepository extends JpaRepository<Member, Long>{
	
	@Query("select m from Member m where m.memberId = :memberId ")
	public Optional<Member> findByMemberId(String memberId);
}
