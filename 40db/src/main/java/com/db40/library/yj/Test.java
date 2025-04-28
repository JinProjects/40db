package com.db40.library.yj;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
@Controller
public class Test {
	//private int no;
	@RequestMapping("/main")
	public String test() {
		return "main";
	}
	
}
