package uni.social.connect.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * We always want to return the code 401 UNAUTHORIZED, because on REST we don't
 * have forwarding
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException exc)
			throws IOException, ServletException {
		if (req.getRequestURI().equals("/login")) {
			return;
		}
		if (exc != null) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			PrintWriter writer = res.getWriter();

			String stringRes = "{\"message\":\"" + exc.getMessage() + "\"}";
			writer.write(stringRes);
			writer.flush();
			return;
		} else {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
