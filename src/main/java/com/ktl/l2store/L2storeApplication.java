package com.ktl.l2store;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.service.user.UserService;

@SpringBootApplication
@ConfigurationPropertiesScan("com.ktl.l2store")
public class L2storeApplication {

	public static void main(String[] args) {
		SpringApplication.run(L2storeApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// @Bean
	// CommandLineRunner run(UserService userService) {
	// return arg -> {

	// userService.saveRole(new Role(null, "ROLE_USER"));
	// userService.saveRole(new Role(null, "ROLE_MANAGER"));
	// userService.saveRole(new Role(null, "ROLE_ADMIN"));
	// userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

	// userService.saveUser(
	// User.builder()
	// .username("superadmin")
	// .password("1234")
	// .gender(true)
	// .email("superadmin@gmail.com")
	// .address("NewYork")
	// .dob(ZonedDateTime.now(ZoneId.of("Z")))
	// .roles(new ArrayList<>())
	// .updatedAt(ZonedDateTime.now(ZoneId.of("Z")))
	// .build());
	// userService.saveUser(
	// User.builder()
	// .username("admin")
	// .password("1234")
	// .gender(true)
	// .email("admin@gmail.com")
	// .address("NewYork")
	// .dob(ZonedDateTime.now(ZoneId.of("Z")))
	// .roles(new ArrayList<>())
	// .updatedAt(ZonedDateTime.now(ZoneId.of("Z")))
	// .build());
	// userService.saveUser(
	// User.builder()
	// .username("manager")
	// .password("1234")
	// .gender(true)
	// .email("manager@gmail.com")
	// .address("NewYork")
	// .roles(new ArrayList<>())
	// .dob(ZonedDateTime.now(ZoneId.of("Z")))
	// .updatedAt(ZonedDateTime.now(ZoneId.of("Z")))
	// .build());

	// userService.addRoleToUser("superadmin", "ROLE_SUPER_ADMIN");
	// userService.addRoleToUser("superadmin", "ROLE_MANAGER");
	// userService.addRoleToUser("admin", "ROLE_ADMIN");
	// userService.addRoleToUser("manager", "ROLE_MANAGER");
	// };
	// }

}
