package com.db40.library.yj.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.db40.library.member.Member;
import com.db40.library.sh.Books;
import com.db40.library.sh.BooksRepository;


@Service
public class AdminService {
	 
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	BooksRepository booksRepository;
	
	public Page<Books> bookGetPaging(int page){  // 어디서부터
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("bookNo"));  // id를 기준으로 내림차순, 
		
		Pageable  pageable = PageRequest.of(page, 10 , Sort.by(sorts));
		return   booksRepository.findAll(pageable);
	}
	public Page<Member> memberGetPaging(int page){  // 어디서부터
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("id"));  // id를 기준으로 내림차순, 
		
		Pageable  pageable = PageRequest.of(page, 10 , Sort.by(sorts));
		return   adminRepository.findAll(pageable);
	}
	
}
