package edu.umcs.bookystore;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import edu.umcs.bookystore.dtos.UserDto;
import edu.umcs.bookystore.entities.RoleEntity;
import edu.umcs.bookystore.exceptions.UsernameAlreadyExistsException;
import edu.umcs.bookystore.repositories.RoleRepository;
import edu.umcs.bookystore.services.UserService;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

	private UserService userService;
	private RoleRepository roleRepository;

	@Value("${spring.security.user.name}")
	public String DEFAULT_ADMIN_EMAIL;

	@Value("${spring.security.user.password}")
	public String DEFAULT_ADMIN_PASSWORD;

	@Value("${spring.security.user.roles}")
	public List<String> DEFAULT_ADMIN_ROLES;

	public CommandLineAppStartupRunner(RoleRepository roleRepository, UserService userService) {
		this.roleRepository = roleRepository;
		this.userService = userService;
	}

	private RoleEntity assertRoleExists(String roleName, String roleDescription) {
		if (roleName == null || roleName.isBlank()) {
			throw new IllegalArgumentException("Role name cannot be null or blank");
		}
		Optional<RoleEntity> roleSearch = this.roleRepository.findByName(roleName);
		if (roleSearch.isPresent()) {
			return roleSearch.get();
		}
		RoleEntity role = new RoleEntity(roleName, roleDescription);
		role = this.roleRepository.save(role);
		return role;
	}

	private RoleEntity assertRoleExists(String roleName) {
		return assertRoleExists(roleName, null);
	}

	private void assertDefaultRolesExist() {
		assertRoleExists("ADMIN", "The site administrator");
		assertRoleExists("USER", "The site ordinary user");
		for (String role : DEFAULT_ADMIN_ROLES) {
			role = role.toUpperCase();
			assertRoleExists(role);
		}
	}

	private void assertAdminExists() {
		if (userService.userExists(DEFAULT_ADMIN_EMAIL)) {
			return;
		}
		UserDto admin = new UserDto(
				DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD,
				null, null);
		Set<String> roles = DEFAULT_ADMIN_ROLES
				.stream()
				.map(role -> role.toUpperCase())
				.collect(Collectors.toSet());
		try {
			this.userService.createUser(admin, roles);
		} catch(UsernameAlreadyExistsException e) {
			throw new RuntimeException("Unexpected behavior", e);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		assertDefaultRolesExist();
		assertAdminExists();
	}
}
