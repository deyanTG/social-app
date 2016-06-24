package uni.social.connect.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import uni.social.connect.Application;

public class CustomTokenBasedRememberMeService extends PersistentTokenBasedRememberMeServices {

	public CustomTokenBasedRememberMeService(String key, UserDetailsService userDetailsService,
			PersistentTokenRepository tokenRepository) {
		super(key, userDetailsService, tokenRepository);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
		String paramValue = (String) request.getAttribute(Application.Constants.REMEMBER_ME_PARAMETER_AND_COOKIE_NAME);

		if (paramValue != null) {
			if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
					|| paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
				return true;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Did not send remember-me cookie (principal did not set parameter '" + parameter + "')");
		}

		return false;
	}
}
