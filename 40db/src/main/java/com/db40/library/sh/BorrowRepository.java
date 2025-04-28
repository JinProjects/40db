package com.db40.library.sh;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.db40.library.member.*;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
	
	@Query("select b from Borrow b where member_id = :memberId")
	List<Borrow> findByMemberId(Member memberId);
	List<Borrow> findByBorrowState(String string);
	

	
}
