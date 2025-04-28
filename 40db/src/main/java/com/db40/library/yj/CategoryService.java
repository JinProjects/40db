package com.db40.library.yj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db40.library.sh.Category;

@Service
public class CategoryService {
	@Autowired
	AdminBooksRepository booksRepository;
	@Autowired
	CategoryRepository categoryRepository;
	
	public Category findCategory(Long id) {
		return categoryRepository.findById(id).get();
	}
}
