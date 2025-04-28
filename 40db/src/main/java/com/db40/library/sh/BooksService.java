package com.db40.library.sh;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BooksService {
	
	private final BooksRepository booksRepository;
	
	public Page<Books> getList(int page) {
		Pageable pageable = PageRequest.of(page, 10);
		return this.booksRepository.findAll(pageable);
	}
	
	public Page<Books> getSearch(int page, String keyword) {
		Pageable pageable = PageRequest.of(page, 10);
		return this.booksRepository.findByBookTitleContainingIgnoreCaseOrBookAuthorContainingIgnoreCaseOrBookPublisherContainingIgnoreCase(keyword, keyword, keyword, pageable);
	}
	
	public Books getBooks (Long bookNo) {
		Optional<Books> books = this.booksRepository.findById(bookNo);
			return books.get();
    }


}