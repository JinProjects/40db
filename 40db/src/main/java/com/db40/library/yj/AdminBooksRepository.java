package com.db40.library.yj;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.db40.library.sh.Books;

public interface AdminBooksRepository extends JpaRepository<Books, Long>{
	@Query("select count(b) from Books b where book_isbn = :isbn")
	public int findByIsbn(String isbn); 
	
	@Query("select b from Books b where b.bookTitle like %:keyword% or b.bookAuthor like %:keyword% order by b.bookNo desc ")
	public List<Books> findByTitleOrAuthor(String keyword);
	
	public List<Books> findAllByOrderByBookNoDesc();
}
