package com.db40.library.sh;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowService {
	@Autowired
	BorrowRepository borrowRepository;
	
	public Page<Borrow> getPaging(int page){  // 어디서부터
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("borrowNo"));  // borrowNo를 기준으로 내림차순, 
		
		Pageable  pageable = PageRequest.of(page, 10);
		return   borrowRepository.findAllOrderByBorrowStateDesc(pageable);
	}

}
