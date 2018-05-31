package com.saturn.web.rest;

import com.saturn.config.Constants;
import com.codahale.metrics.annotation.Timed;
import com.saturn.domain.User;
import com.saturn.repository.UserRepository;
import com.saturn.service.UserService;
import com.saturn.service.dto.UserDTO;
import com.saturn.web.rest.util.HeaderUtil;
import com.saturn.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for managing users.
 *
 */
@RestController
@RequestMapping("/api")
public class UserResource {

	private final Logger log = LoggerFactory.getLogger(UserResource.class);

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserService userService;

	/**
	 * POST /users : Creates a new user.
	 * <p>
	 * Creates a new user if the login and email are not already used. The user needs to be activated on creation.
	 * </p>
	 *
	 * @param userDTO the user to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request)
	 * if the login or email is already in use
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/users")
	@Timed
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
		log.debug("REST request to save User : {}", userDTO);

		//Lowercase the user login before comparing with database
		if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
			return ResponseEntity.badRequest()
				.headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use"))
				.body(null);
		} else if (userRepository.findOneByEmail(userDTO.getEmail()).isPresent()) {
			return ResponseEntity.badRequest()
				.headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
				.body(null);
		} else {
			User newUser = userService.createUser(userDTO.getLogin(), userDTO.getPassword(),
				userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getEmail().toLowerCase(), userDTO.getLangKey());
			return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
				.headers(HeaderUtil.createAlert("A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
				.body(newUser);
		}
	}

	/**
	 * PUT /users : Updates an existing User.
	 *
	 * @param userDTO the user to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated user, or with status 400 (Bad Request)
	 * if the login or email is already in use, or with status 500 (Internal Server Error) if the user couldn't be
	 * updated
	 */
	@PutMapping("/users")
	@Timed
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
		log.debug("REST request to update User : {}", userDTO);
		Optional<User> existingUser = userRepository.findOneByEmail(userDTO.getEmail());
		if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "E-mail already in use")).body(null);
		}
		existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
		if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use")).body(null);
		}
		userService.updateUser(userDTO.getId(), userDTO.getLogin(), userDTO.getFirstName(),
			userDTO.getLastName(), userDTO.getEmail(), userDTO.isActivated(),
			userDTO.getLangKey(), userDTO.getRole());

		return ResponseEntity.ok()
			.headers(HeaderUtil.createAlert("A user is updated with identifier " + userDTO.getLogin(), userDTO.getLogin()))
			.body(new UserDTO(userService.getUser(userDTO.getId())));
	}

	/**
	 * GET /users : get all users.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and with body all users
	 * @throws URISyntaxException if the pagination headers couldn't be generated
	 */
	@GetMapping("/users")
	@Timed
	public ResponseEntity<List<UserDTO>> getAllUsers(@ApiParam Pageable pageable)
		throws URISyntaxException {
		Page<User> page = userRepository.findAll(pageable);
		List<UserDTO> userDTO = page.getContent().stream().map(UserDTO::new).collect(Collectors.toList());
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
		return new ResponseEntity<>(userDTO, headers, HttpStatus.OK);
	}

	/**
	 * GET /users/:login : get the "login" user.
	 *
	 * @param login the login of the user to find
	 * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
	 */
	@GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
	@Timed
	public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
		log.debug("REST request to get User : {}", login);
		return userService.getUser(login)
			.map(UserDTO::new)
			.map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
			.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /users/:login : delete the "login" User.
	 *
	 * @param login the login of the user to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
	@Timed
	@PreAuthorize("hasAuthority(T(com.saturn.domain.util).ADMIN)")
	public ResponseEntity<Void> deleteUser(@PathVariable String login) {
		log.debug("REST request to delete User: {}", login);
		userService.deleteUser(login);
		return ResponseEntity.ok().headers(HeaderUtil.createAlert("A user is deleted with identifier " + login, login)).build();
	}
}
