package uni.social.connect.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.glasnost.orika.MapperFacade;
import uni.social.connect.dto.UserOutput;
import uni.social.connect.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private RequestCache requestCache = new HttpSessionRequestCache();

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MapperFacade mapper;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws ServletException, IOException {
		final SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			clearAuthenticationAttributes(request);
			returnCurrentUserAsResponse(response);
			return;
		}
		final String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			clearAuthenticationAttributes(request);
			returnCurrentUserAsResponse(response);
			return;
		}

		clearAuthenticationAttributes(request);
		returnCurrentUserAsResponse(response);
	}

	public void setRequestCache(final RequestCache requestCache) {
		this.requestCache = requestCache;
	}

	private void returnCurrentUserAsResponse(HttpServletResponse r) throws IOException {
		uni.social.connect.model.User u = ((UserDetailsServiceImpl.Account) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();
		UserOutput userOutput = mapper.map(u, UserOutput.class);
		Writer writer = r.getWriter();
		String stringRes = "{\"data\":" + objectMapper.writeValueAsString(userOutput) + "}";
		writer.write(stringRes);
		writer.flush();
	}
}
