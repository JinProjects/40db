package com.db40.library.yj;

import java.util.Optional;

import javax.persistence.Column;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.db40.library.sh.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{
	@Column(name = "book_category_name")
	Optional<Category> findByBookCategoryName(String book_category_name);
	
	@Query(value = "select c from category c where c.book_category_name = :book_category_name", nativeQuery = true)
	//@Query(value = "select c from category c where c.book_category_name = :book_category_name")
	public Category findBybookCategory(String book_category_name);
	
}
