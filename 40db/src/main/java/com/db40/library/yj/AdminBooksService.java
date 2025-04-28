package com.db40.library.yj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db40.library.sh.Books;

@Service
public class AdminBooksService {
	@Autowired
	AdminBooksRepository booksRepository;
	
	public void insert(Books books) {
	//	books.setCategory( //카테고리에 값이 존재하야 에러 안남
		//		);
		booksRepository.save(books);
	}
	
	//public Category findCategory(String name) {
		//booksRepository.find
	//}
}
