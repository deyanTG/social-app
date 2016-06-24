package uni.social.connect.utils;

import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import uni.social.connect.exception.ItemNotFoundException;
import uni.social.connect.model.User;
import uni.social.connect.service.UserDetailsServiceImpl;

public class UserUtils {

	public static User getCurrentUser() {
		return ((UserDetailsServiceImpl.Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUser();
	}

	public static Long getCurrentUserId() {
		return getCurrentUser().getId();
	}

	public static String getCurrentUserAgent() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getHeader("user-agent");
	}

	public static String getCurrentUserIPAddress() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getRemoteAddr();
	}

	public static String getCurrentUserSessionId() {
		return RequestContextHolder.currentRequestAttributes().getSessionId();
	}

	@SuppressWarnings("unchecked")
	public static Collection<SimpleGrantedAuthority> getCurrentUserAuthorities() {
		return (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities();
	}

	public static User getUserFromProvider(Connection<?> connection) {
		User user = new User();
		UserProfile providerUser = connection.fetchUserProfile();
		user.setFirstName(providerUser.getFirstName());
		user.setLastName(providerUser.getLastName());

		// TODO : change if not appropriate
		// If email is not fetch from social provider just throw an Exception
		if (providerUser.getEmail() == null) {
			throw new ItemNotFoundException(User.class, null);
		}
		user.setUsername(providerUser.getEmail());
		user.setRole(User.Role.COMPANY_USER);
		return user;
	}

}
