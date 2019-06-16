package org.mahmoudi.service;

import org.mahmoudi.dao.AppRoleRepository;
import org.mahmoudi.dao.AppUserRepository;
import org.mahmoudi.entities.AppRole;
import org.mahmoudi.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppRoleRepository appRoleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Override
	public AppUser saveUser(String username, String password, String confirmedPassword) {
		AppUser user = appUserRepository.findByUsername(username);
		if(user!=null) throw new RuntimeException("User already exists!");
		if(!password.equals(confirmedPassword)) throw new RuntimeException("The passwords do not match!");
		AppUser newUser = new AppUser();
		newUser.setUsername(username);
		newUser.setPassword(bCryptPasswordEncoder.encode(password));
		newUser.setActived(true);
		appUserRepository.save(newUser);
		addRoleToUser(username, "USER");
		return loadUserByUsername(username);
	}

	@Override
	public AppRole saveRole(AppRole role) {
		return appRoleRepository.save(role);
	}

	@Override
	public AppUser loadUserByUsername(String username) {
		return appUserRepository.findByUsername(username);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser user = appUserRepository.findByUsername(username);
		AppRole role = appRoleRepository.findByRoleName(roleName);
		user.getRoles().add(role);
	}

}
