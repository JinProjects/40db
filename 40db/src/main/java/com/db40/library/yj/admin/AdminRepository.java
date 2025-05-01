package com.db40.library.yj.admin;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.db40.library.member.Member;

public interface AdminRepository extends JpaRepository<Member, Long>{
	
	 //Page...
		Page<Member> findAll(Pageable pageable);
	@Query("select m from Member m where m.memberId = :memberId ")
	public Optional<Member> findByMemberId(String memberId);
	
	@Query("select m from Member m where m.memberId = :memberId order by id desc")
	public Page<Member> findAllByMemberIdOrderbyIdDesc(Pageable  pageable, String memberId);
	
	@Modifying 
	@Transactional 
	@Query("delete from Member m where m.memberId = :memberId")
	public void deleteByMemberId(String memberId);
}
