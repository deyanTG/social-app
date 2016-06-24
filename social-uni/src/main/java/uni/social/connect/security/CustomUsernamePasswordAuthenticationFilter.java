package uni.social.connect.security;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;

import uni.social.connect.Application;

@Component
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		String password = null;
		if ("application/json".equals(request.getHeader("Content-Type"))) {
			password = (String) request.getAttribute("password_param");
		} else {
			password = super.obtainPassword(request);
		}
		return password;
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		String username = null;
		if ("application/json".equals(request.getHeader("Content-Type"))) {
			username = (String) request.getAttribute("username_param");
		} else {
			username = super.obtainUsername(request);
		}
		return username;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		if ("application/json".equals(request.getHeader("Content-Type"))) {
			try {
				// Here we parse request body and populate needed attributes for
				// authentication and eventually for remember me functionality
				String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
				JsonNode rootNode = objectMapper.readTree(body);
				JsonNode usernameNode = rootNode.path("username");
				JsonNode passwordNode = rootNode.path("password");

				JsonNode rememberMe = rootNode.path(Application.Constants.REMEMBER_ME_PARAMETER_AND_COOKIE_NAME);
				if (!MissingNode.getInstance().equals(rememberMe)) {
					request.setAttribute(Application.Constants.REMEMBER_ME_PARAMETER_AND_COOKIE_NAME,
							rememberMe.asText());
				}

				request.setAttribute("username_param", usernameNode.asText());
				request.setAttribute("password_param", passwordNode.asText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return super.attemptAuthentication(request, response);
	}
}
