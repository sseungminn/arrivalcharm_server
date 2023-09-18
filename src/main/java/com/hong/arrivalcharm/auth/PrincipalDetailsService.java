package com.hong.arrivalcharm.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.repository.UserRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntiry = userRepository.findByUsername(username);
		if(userEntiry != null) {
			return new PrincipalDetails(userEntiry);
		}
		return null;
	}

}
