package uni.social.connect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import ma.glasnost.orika.MapperFacade;
import uni.social.connect.component.JsonArg;
import uni.social.connect.dto.UserOutput;
import uni.social.connect.model.AccountProvider;
import uni.social.connect.model.User;
import uni.social.connect.service.UserDetailsServiceImpl;
import uni.social.connect.service.UserService;
import uni.social.connect.utils.SecurityUtils;
import uni.social.connect.utils.UserUtils;

@RestController
public class AccountController {

	@Autowired
	private UserService userService;
	
    @Autowired
    private MapperFacade mapper;


	@Autowired
	private ProviderSignInUtils providerSignInUtils;

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public User registerLocalUserAccount(@JsonArg String email, @JsonArg String password, @JsonArg String firstName,
			@JsonArg String lastName, WebRequest request) {
		return userService.registerNewUserAccount(email, password, firstName, lastName);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public User registerSocialUserAccount(WebRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
			User user = UserUtils.getUserFromProvider(connection);
			user = userService.registerNewUserSocialAccount(user, request,AccountProvider.valueOf(connection.getKey().getProviderId().toUpperCase()));
			return user;
		}
		return null;
	}

	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.POST)
	public void confirmRegistration(@JsonArg String token) {
		userService.confirmRegistration(token);
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public void resetPassword(@JsonArg String email) {
		userService.resetPasswordForAccount(email);

	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public void changePassword(@JsonArg String token, @JsonArg Long userId) {
		userService.changePassword(token, userId);

	}

	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
	public void savePassword(@JsonArg String password) {
		userService.changeUserPassword(password);
	}

	@RequestMapping(value = "/session", method = RequestMethod.GET)
    public UserOutput getSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SessionAuthenticationException("Credentials expired");
        }
        if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            return mapper.map(new User(User.Role.ANONYMOUS), UserOutput.class);
        }
        return mapper.map(UserUtils.getCurrentUser(), UserOutput.class);
    }
}
