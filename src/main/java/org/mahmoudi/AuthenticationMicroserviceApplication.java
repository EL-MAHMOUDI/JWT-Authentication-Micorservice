package org.mahmoudi;

import java.util.stream.Stream;

import org.mahmoudi.entities.AppRole;
import org.mahmoudi.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AuthenticationMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationMicroserviceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner start(AccountService service) {
		return args->{
			service.saveRole(new AppRole(null, "ADMIN"));
			service.saveRole(new AppRole(null, "USER"));
			Stream.of("user1","user2","user3","admin").forEach(username->{
				service.saveUser(username, "1234", "1234").toString();
			});
			service.addRoleToUser("admin", "ADMIN");
		};
	}
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
