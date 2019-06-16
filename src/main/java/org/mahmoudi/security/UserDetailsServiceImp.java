package org.mahmoudi.security;

import java.util.ArrayList;
import java.util.Collection;

import org.mahmoudi.entities.AppUser;
import org.mahmoudi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
	@Autowired
	private AccountService service;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = service.loadUserByUsername(username);
		if(user==null) throw new RuntimeException("Invalid user");
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		user.getRoles().forEach(r->{
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
		});
		return new User(user.getUsername(), user.getPassword(),authorities);
	}

}
