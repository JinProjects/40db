package com.db40.library.sh;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="book_category_no")
	private Long bookCategoryNo;
	
	@Column(name="book_category_name")
	private String bookCategoryName;
	
	@OneToMany(mappedBy = "category")
	List<Books> books = new ArrayList<>();
}