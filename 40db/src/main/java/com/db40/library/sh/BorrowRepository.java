package com.db40.library.sh;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.db40.library.member.Member;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
	
	@Query("select b from Borrow b where member_id = :memberId")
	List<Borrow> findByMemberId(Member memberId);
	List<Borrow> findByBorrowState(String string);
	
	@Query("SELECT b FROM Borrow b ORDER BY CASE b.borrowState WHEN '연체' THEN 1 WHEN '대출 중' THEN 2 WHEN '정상 반납' THEN 3"  
			    + " WHEN '반납 완료' THEN 4 " 
			    + " ELSE 99 "             
			    + " END ASC, "
			    + " b.overdueDays DESC")
	public Page<Borrow> findAllOrderByBorrowStateDesc(Pageable pageable);

	
}
