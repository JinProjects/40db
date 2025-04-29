package com.db40.library.member;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration      // 스프링부트 환경설정팡리
@EnableWebSecurity  // url 스프링시큐리티 제어 - SecurityFilterChain
public class SecurityConfig {
    //1. url
	@Bean SecurityFilterChain  filterChain(HttpSecurity http) throws Exception{
		// 1-0. http.authorizeHttpRequests().formLogin().logout();
		// 1-1. 로그인을 안하더라도 모든페이지 접근가능   /admin/**   , /user/**,,,	
		http.csrf().disable().authorizeHttpRequests(
			(authorizeHttpRequests) -> 	authorizeHttpRequests
											// admin 만 접근가능
											//.requestMatchers(  new AntPathRequestMatcher("/admin/**"))
											//.hasRole("ROLE_ADMIN") 	  // ADMIN 역할
			
											// member 만 접근가능
											.requestMatchers(  new AntPathRequestMatcher("/admin/**"))
											.hasRole("ADMIN") 	  // MEMBER 역할
											.requestMatchers(  new AntPathRequestMatcher("/member/mypage/**"))
											.hasAnyRole("ADMIN","MEMBER") 	  // MEMBER 역할
											// 기타페이지 모두 접근가능( 로그인 필요 없음)
											.requestMatchers(  new AntPathRequestMatcher("/**"))
											.permitAll() // 모든사용자 접근가능		  
		).formLogin(  // 1-2. form 만든폼 - login
			(formLogin)-> 	formLogin
								.loginPage("/member/login")
								.loginProcessingUrl("/member/login")
								.usernameParameter("memberId")
								.passwordParameter("memberPass")
								.defaultSuccessUrl("/member/mypage/main")
		).logout( // 1-3. logout
			(logout)-> 	 logout
							.logoutRequestMatcher( new AntPathRequestMatcher("/member/logout"))
							.logoutSuccessUrl("/member/login")
							.invalidateHttpSession(true)
		); 
		return http.build();
	}
	
	//2. AuthenticationManager - 사용자인증시 Service와 PasswordEncoder를 사용함.
	@Bean AuthenticationManager   authenticationManager(AuthenticationConfiguration  authenticationConfiguration) throws Exception{  
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	//3. PasswordEncoder - 비번암호화
	@Bean  	PasswordEncoder  passwordEncoder() { return new  BCryptPasswordEncoder(); }

	public static void main(String[] args) {
		
	}
} 

// Bean - 스크링이 관리하는 객체