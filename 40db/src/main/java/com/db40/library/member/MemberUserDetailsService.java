package com.db40.library.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService{
	private final MemberRepository   memberRepository;
	
	@Override public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		
		
		Optional<Member> find = memberRepository.findByMemberId(memberId);
		if(find.isEmpty()) {  throw new UsernameNotFoundException("사용자를 확인해주세요."); }
		
		Member member = find.get();
		
		List<GrantedAuthority>  authorities = new ArrayList<>();
		if( "admin".equals(memberId)) {
			authorities.add( new SimpleGrantedAuthority( MemberRole.ADMIN.getValue() ));
		}else {
			authorities.add( new SimpleGrantedAuthority(MemberRole.MEMBER.getValue()));
		} 
		return new User(  member.getMemberId() , member.getMemberPass(), authorities);
	}

} 
