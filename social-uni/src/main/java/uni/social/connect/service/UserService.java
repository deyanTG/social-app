package uni.social.connect.service;

import ma.glasnost.orika.MapperFacade;
import uni.social.connect.dao.PasswordResetTokenRepository;
import uni.social.connect.dao.UserRepository;
import uni.social.connect.dao.VerificationTokenRepository;
import uni.social.connect.dto.UserInput;
import uni.social.connect.event.OnRegistrationCompleteEvent;
import uni.social.connect.exception.EmailExistsException;
import uni.social.connect.exception.ItemNotFoundException;
import uni.social.connect.exception.PasswordResetTokenExpiredException;
import uni.social.connect.exception.VerificationTokenExpiredException;
import uni.social.connect.model.*;
import uni.social.connect.utils.SecurityUtils;
import uni.social.connect.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Tan on 18-Apr-16. User service class
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private MapperFacade mapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("mailSender")
	private MailSender mailSender;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private ProviderSignInUtils signInUtils;

	public User get(Long id) {
		User user = userRepository.findOneById(id);
		if (user == null) {
			throw new ItemNotFoundException(User.class, id);
		}
		return user;
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public User update(UserInput input) {
		User user = get(input.getId());
		mapper.map(input, user);
		return userRepository.save(user);
	}

	public User registerNewUserAccount(String email, String password, String firstName, String lastName)
			throws EmailExistsException {
		if (emailExist(email)) {
			throw new EmailExistsException("There is an account with that email adress: " + email);
		}
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setPassword(passwordEncoder.encode(password));
		user.setUsername(email);
		user.setRole(User.Role.COMPANY_USER);
		User u = userRepository.save(user);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, Locale.US, "http://localhost:17001"));
		return u;
	}

	private boolean emailExist(String email) {
		User user = userRepository.findOneByUsername(email);
		if (user != null) {
			return true;
		}
		return false;
	}

	public User getUser(String verificationToken) {
		User user = tokenRepository.findByToken(verificationToken).getUser();
		return user;
	}

	public VerificationToken getVerificationToken(String VerificationToken) {
		return tokenRepository.findByToken(VerificationToken);
	}

	public void createVerificationToken(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}

	public void confirmRegistration(String token) {
		VerificationToken verificationToken = this.getVerificationToken(token);
		if (verificationToken == null) {
			throw new ItemNotFoundException(VerificationToken.class, null);
		}
		User user = verificationToken.getUser();
		LocalDateTime timeNow = LocalDateTime.now();
		if (verificationToken.getExpiryDate().isBefore(timeNow)) {
			throw new VerificationTokenExpiredException();
		}
		verificationToken.setUsed(true);
		user.setEnabled(true);
	}

	public void resetPasswordForAccount(String email) {
		User user = userRepository.findOneByUsername(email);

		if (user == null) {
			throw new ItemNotFoundException(User.class, null);
		}

		String token = UUID.randomUUID().toString();

		createPasswordResetTokenForUser(user, token);

		SimpleMailMessage mailMessage = constructResetTokenEmail(token, user);
		mailSender.send(mailMessage);
	}

	private void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken resetToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(resetToken);
	}

	private SimpleMailMessage constructResetTokenEmail(String token, User user) {
		String message = "token=" + token + "&" + "id=" + user.getId();
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getUsername());
		email.setSubject("Reset Password");
		email.setText(message);
		return email;
	}

	public void changePassword(String token, Long id) {
		PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
		if (passToken == null) {
			throw new ItemNotFoundException(PasswordResetToken.class, null);
		}

		User userOfResetToken = passToken.getUser();

		if (!userOfResetToken.getId().equals(id)) {
			throw new UsernameNotFoundException("Token has been issued for another user");
		}

		LocalDateTime now = LocalDateTime.now();
		if (passToken.getExpiryDate().isBefore(now)) {
			throw new PasswordResetTokenExpiredException();
		}

		UserDetails authentication = new UserDetailsServiceImpl.Account(userOfResetToken);

		Authentication auth = new UsernamePasswordAuthenticationToken(authentication, null,
				authentication.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public void changeUserPassword(String password) {
		User current = UserUtils.getCurrentUser();
		current.setPassword(passwordEncoder.encode(password));
	}

	public User registerNewUserSocialAccount(User user, WebRequest request, AccountProvider accountProvider) {
		User alreadyRegisteredUser = userRepository.findOneByUsername(user.getUsername());
		if (alreadyRegisteredUser == null) {
			alreadyRegisteredUser = userRepository.save(user);
		}
		signInUtils.doPostSignUp(alreadyRegisteredUser.getUsername(), request);
		SecurityUtils.authenticateSocialUser(new UserDetailsServiceImpl.Account(alreadyRegisteredUser));
		return alreadyRegisteredUser;
	}
}
