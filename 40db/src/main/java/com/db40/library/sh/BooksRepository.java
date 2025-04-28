package com.db40.library.sh;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BooksRepository extends JpaRepository<Books, Long> {

	Books findByBookNo(Long bookNo);
	
	Page<Books> findByBookTitleContainingIgnoreCaseOrBookAuthorContainingIgnoreCaseOrBookPublisherContainingIgnoreCase(String bookTitleKeyword, String bookAuthorKeyword, String bookPublisherKeyword, Pageable pageable);


}
