package com.ktl.l2store;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.service.user.UserService;

@SpringBootApplication
public class L2storeApplication {

	public static void main(String[] args) {
		SpringApplication.run(L2storeApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return arg -> {
			// userService.saveRole(new Role(null, "ROLE_USER", new ArrayList<>()));
			// userService.saveRole(new Role(null, "ROLE_MANAGER", new ArrayList<>()));
			// userService.saveRole(new Role(null, "ROLE_ADMIN", new ArrayList<>()));
			// userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN", new ArrayList<>()));

			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_MANAGER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			userService.saveUser(new User(null, "superadmin", "superadmin", "superadmin@gmail.com", "1234", true,
					LocalDateTime.now(), new ArrayList<>(), LocalDateTime.now()));
			userService.saveUser(new User(null, "admin", "admin", "admin@gmail.com", "1234", true, LocalDateTime.now(),
					new ArrayList<>(), LocalDateTime.now()));
			userService.saveUser(new User(null, "manager", "manager", "manager@gmail.com", "1234", true,
					LocalDateTime.now(), new ArrayList<>(), LocalDateTime.now()));
			userService.saveUser(new User(null, "user", "user", "user@gmail.com", "1234", true, LocalDateTime.now(),
					new ArrayList<>(), LocalDateTime.now()));

			userService.addRoleToUser("superadmin", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("superadmin", "ROLE_MANAGER");
			userService.addRoleToUser("admin", "ROLE_ADMIN");
			userService.addRoleToUser("manager", "ROLE_MANAGER");
			userService.addRoleToUser("user", "ROLE_USER");
		};
	}

}
