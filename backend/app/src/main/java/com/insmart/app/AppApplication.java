package com.insmart.app;

import com.insmart.app.domain.Permission;
import com.insmart.app.domain.Resource;
import com.insmart.app.domain.Role;
import com.insmart.app.domain.User;
import com.insmart.app.repo.PermissionRepo;
import com.insmart.app.repo.ResourceRepo;
import com.insmart.app.repo.RoleRepo;
import com.insmart.app.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;

@SpringBootApplication
@SuppressWarnings("unused")
@Transactional
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner run(UserService userService, ResourceRepo resourceRepo, PermissionRepo permissionRepo, RoleRepo roleRepo) {
		return args -> {
			userService.save(new Role(null, "ROLE_USER"));
			userService.save(new Role(null, "ROLE_ADMIN"));
			userService.save(new User(null, "lã văn thọ", "john","1234","tholv@ftl.vn","ADMIN","", new ArrayList<>(),null));
			userService.save(new User(null, "lã văn thọ", "tholv","1234","tholv@ftl.vn","ADMIN1","", new ArrayList<>(),null));
			userService.addRoleToUser("john", "ROLE_USER");
			userService.addRoleToUser("tholv", "ROLE_ADMIN");
			Resource resource1 = new Resource(null, "1", "resource", "/admin/resource-manager",null, null);
			resourceRepo.save(resource1);
			Resource resource2 = new Resource(null, "123", "user", "/admin/user-manager",null, null);
			resourceRepo.save(resource2);
			Permission permission  = new Permission(null,userService.findByUsername("john"),resource1,true,true,true,true);
			Permission permission1  = new Permission(null,userService.findByUsername("tholv"),resource1,false,true,true,true);
			permissionRepo.save(permission);
			permissionRepo.save(permission1);
		};
	}

}

