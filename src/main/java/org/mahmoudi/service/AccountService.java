package org.mahmoudi.service;

import org.mahmoudi.entities.AppRole;
import org.mahmoudi.entities.AppUser;

public interface AccountService{
	public AppUser saveUser(String username, String password, String confirmedPassword);
	public AppRole saveRole(AppRole role);
	public AppUser loadUserByUsername(String username);
	public void addRoleToUser(String username, String roleName);
}
