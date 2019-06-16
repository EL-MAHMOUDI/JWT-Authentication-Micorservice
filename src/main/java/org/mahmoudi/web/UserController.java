package org.mahmoudi.web;

import org.mahmoudi.entities.AppUser;
import org.mahmoudi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController
public class UserController {
	@Autowired
	private AccountService service;
	@PostMapping("/register")
	public AppUser register(@RequestBody UserForm userForm) {
		return service.saveUser(userForm.getUsername(), userForm.getPassword(), userForm.getConfirmedPassword());
	}
}

@Data
class UserForm{
	private String username;
	private String password;
	private String confirmedPassword;
}