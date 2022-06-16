package edu.umcs.bookystore.services;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetailsService;

import edu.umcs.bookystore.dtos.UserDto;
import edu.umcs.bookystore.exceptions.UsernameAlreadyExistsException;

public interface UserService extends UserDetailsService {

	void createUser(UserDto user) throws UsernameAlreadyExistsException;

	void createUser(UserDto user, Set<String> roles) throws UsernameAlreadyExistsException;

	void updateUser(UserDto user);

	void updateUser(UserDto user, Set<String> roles);

	void deleteUser(String username);

	boolean userExists(String username);
}
