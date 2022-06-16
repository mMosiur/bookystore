package edu.umcs.bookystore.services.implementations;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.umcs.bookystore.dtos.UserDto;
import edu.umcs.bookystore.entities.RoleEntity;
import edu.umcs.bookystore.entities.UserEntity;
import edu.umcs.bookystore.exceptions.UsernameAlreadyExistsException;
import edu.umcs.bookystore.repositories.RoleRepository;
import edu.umcs.bookystore.repositories.UserRepository;
import edu.umcs.bookystore.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = this.userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(
						String.format("User with username \"%s\" not found", username)));
		return User
				.withUsername(user.getUsername())
				.password(user.getPasswordHash())
				.roles(getRoles(user))
				.build();
	}

	private String[] getRoles(UserEntity user) {
		return user.getRoles().stream()
				.map(role -> role.getName())
				.toArray(String[]::new);
	}

	@Override
	public void createUser(UserDto user) throws UsernameAlreadyExistsException {
		Set<String> roles = new HashSet<>();
		roles.add("USER");
		createUser(user, roles);
	}

	@Override
	public void createUser(UserDto user, Set<String> roles) throws UsernameAlreadyExistsException {
		if (this.userExists(user.getEmail())) {
			throw new UsernameAlreadyExistsException(
					String.format("User with username \"%s\" already exists", user.getEmail()));
		}
		UserEntity userEntity = new UserEntity(
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				passwordEncoder.encode(user.getPassword()),
				getRoleEntities(roles));
		userEntity = this.userRepository.save(userEntity);
		user.setPassword(userEntity.getPasswordHash());
	}

	private UserEntity getUserEntity(UserDto user) throws UsernameNotFoundException {
		return this.userRepository.findByEmail(user.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException(
						String.format("User with email \"%s\" not found", user.getEmail())));
	}

	private Set<RoleEntity> getRoleEntities(Set<String> roleNames) {
		return roleNames.stream().map(roleName -> this.roleRepository.findByName(roleName)
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("Role with name \"%s\" not found", roleName))))
				.collect(Collectors.toSet());
	}

	@Override
	public void updateUser(UserDto user) {
		updateUser(user, null);
	}

	@Override
	public void updateUser(UserDto user, Set<String> roles) {
		UserEntity userEntity = getUserEntity(user);
		userEntity.setEmail(user.getEmail());
		userEntity.setUsername(user.getEmail());
		userEntity.setPasswordHash(this.passwordEncoder.encode(user.getPassword()));
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		if (roles != null) {
			userEntity.setRoles(getRoleEntities(roles));
		}
		userEntity = this.userRepository.save(userEntity);

	}

	@Override
	public void deleteUser(String username) {
		UserEntity userEntity = this.userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with username \"%s\" not found", username)));
		this.userRepository.delete(userEntity);
	}

	@Override
	public boolean userExists(String username) {
		return this.userRepository.findByUsername(username).isPresent();
	}

}
