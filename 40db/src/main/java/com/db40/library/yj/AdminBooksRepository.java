package com.db40.library.yj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.db40.library.sh.Books;

public interface AdminBooksRepository extends JpaRepository<Books, Long>{
	@Query("select count(b) from Books b where book_isbn = :isbn")
	public int findByIsbn(String isbn); 
	
}
