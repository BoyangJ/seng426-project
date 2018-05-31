package com.saturn.service;

import com.saturn.domain.util.Role;
import com.saturn.domain.User;
import com.saturn.repository.PersistentTokenRepository;
import com.saturn.repository.UserRepository;
import com.saturn.security.SecurityUtils;
import com.saturn.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.inject.Inject;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private UserRepository userRepository;

	@Inject
	private PersistentTokenRepository persistentTokenRepository;

	public Optional<User> activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key);
		return userRepository.findOneByActivationKey(key)
			.map(user -> {
				// activate given user for the registration key.
				user.setActivated(true);
				user.setActivationKey(null);
				log.debug("Activated user: {}", user);
				return user;
			});
	}

	public Optional<User> completePasswordReset(String newPassword, String key) {
		log.debug("Reset user password for reset key {}", key);

		return userRepository.findOneByResetKey(key)
			.filter(user -> {
				ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
				return user.getResetDate().isAfter(oneDayAgo);
			})
			.map(user -> {
				user.setPassword(passwordEncoder.encode(newPassword));
				user.setResetKey(null);
				user.setResetDate(null);
				return user;
			});
	}

	public Optional<User> requestPasswordReset(String mail) {
		return userRepository.findOneByEmail(mail)
			.filter(User::getActivated)
			.map(user -> {
				user.setResetKey(RandomUtil.generateResetKey());
				user.setResetDate(ZonedDateTime.now());
				return user;
			});
	}

	public User createUser(String login, String password, String firstName, String lastName, String email,
		String langKey) {

		User newUser = new User();

		newUser.setLogin(email);
		newUser.setPassword(passwordEncoder.encode(password));

		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);
		newUser.setRole(Role.USER);

		if (langKey == null) {
			newUser.setLangKey("en"); // default language
		} else {
			newUser.setLangKey(langKey);
		}

		// TODO require activation during registration
		newUser.setActivated(true);
		newUser.setActivationKey(null);

		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public void updateUser(String firstName, String lastName, String email, String langKey) {
		userRepository.findOneByLogin(SecurityUtils.getCurrentUser()).ifPresent(user -> {
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setLangKey(langKey);
			log.debug("Changed Information for User: {}", user);
		});
	}

	public void updateUser(Long id, String login, String firstName, String lastName, String email,
		boolean activated, String langKey, Role role) {

		Optional.of(userRepository
			.findOne(id))
			.ifPresent(user -> {
				user.setLogin(login);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setEmail(email);
				user.setActivated(activated);
				user.setLangKey(langKey);
				user.setRole(role);
				log.debug("Changed Information for User: {}", user);
			});
	}

	public void deleteUser(String login) {
		userRepository.findOneByLogin(login).ifPresent(user -> {
			userRepository.delete(user);
			log.debug("Deleted User: {}", user);
		});
	}

	public void changePassword(String password) {
		userRepository.findOneByLogin(SecurityUtils.getCurrentUser()).ifPresent(user -> {
			String encryptedPassword = passwordEncoder.encode(password);
			user.setPassword(encryptedPassword);
			log.debug("Changed password for User: {}", user);
		});
	}

	@Transactional(readOnly = true)
	public Optional<User> getUser(String login) {
		return userRepository.findOneByLogin(login);
	}

	@Transactional(readOnly = true)
	public User getUser(Long id) {
		User user = userRepository.findOne(id);
		return user;
	}

	@Transactional(readOnly = true)
	public User getCurrentUser() {
		return getUser(SecurityUtils.getCurrentUser()).orElse(null);
	}

	/**
	 * Persistent Token are used for providing automatic authentication, they should be automatically deleted after 30
	 * days.
	 * <p>
	 * This is scheduled to get fired everyday, at midnight.
	 * </p>
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void removeOldPersistentTokens() {
		LocalDate now = LocalDate.now();
		persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).forEach(token -> {
			log.debug("Deleting token {}", token.getSeries());
			User user = token.getUser();
			user.getPersistentTokens().remove(token);
			persistentTokenRepository.delete(token);
		});
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 * </p>
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		ZonedDateTime now = ZonedDateTime.now();
		List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
		for (User user : users) {
			log.debug("Deleting not activated user {}", user.getLogin());
			userRepository.delete(user);
		}
	}
}
