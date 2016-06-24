package uni.social.connect.conf;

import uni.social.connect.Application;
import uni.social.connect.security.*;
import uni.social.connect.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;
import java.util.Arrays;

@EnableWebSecurity
@Configuration
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier(value = "userDetailsService")
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private RestAuthenticationSuccessHandler authSuccessHandler;

	@Autowired
	private RestAuthenticationFailureHandler authFailureHandler;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private RestAuthenticationEntryPoint authEntryPoint;

	@Autowired
	private AuthenticationManager manager;

	// TODO : think for another key
	private final static String REMEMBER_ME_KEY = "KEY";

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays.asList(authenticationProvider()));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().anonymous().and().authorizeRequests()
				.antMatchers("/jsondoc", "/registration", "/registrationConfirm", "/resetPassword", "/changePassword",
						"/auth**", "/signup", "/login", "/session", "/resources/**", "/css/**", "/connect/**", "/search/**")
				.permitAll().anyRequest().authenticated();
		http.exceptionHandling().authenticationEntryPoint(authEntryPoint).and().formLogin().loginPage("/login")
				.successHandler(authSuccessHandler).failureHandler(authFailureHandler).and().logout()
				.deleteCookies("JSESSIONID", Application.Constants.REMEMBER_ME_PARAMETER_AND_COOKIE_NAME).permitAll()
				.logoutRequestMatcher(new AntPathRequestMatcher("/login", "DELETE"))
				.logoutSuccessHandler(logoutSuccessHandler()).and().sessionManagement()
				.maximumSessions(Application.Constants.MAXIMUM_SESSIONS_ALLOWED);
		http.rememberMe().rememberMeCookieName(Application.Constants.REMEMBER_ME_PARAMETER_AND_COOKIE_NAME)
				.tokenRepository(persistentTokenRepository())
				.rememberMeServices(persistentTokenBasedRememberMeServices())
				.tokenValiditySeconds(Application.Constants.REMEMBER_ME_IN_SECONDS).and()
				.apply(springSocialConfigurer());
		http.addFilterBefore(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new HttpStatusReturningLogoutSuccessHandler();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
		CustomUsernamePasswordAuthenticationFilter authFilter = new CustomUsernamePasswordAuthenticationFilter();
		authFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
		authFilter.setAuthenticationManager(manager);
		authFilter.setAuthenticationSuccessHandler(authSuccessHandler);
		authFilter.setAuthenticationFailureHandler(authFailureHandler);
		authFilter.setRememberMeServices(persistentTokenBasedRememberMeServices());
		return authFilter;
	}

	@Bean
	public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
		return new CustomTokenBasedRememberMeService(REMEMBER_ME_KEY, userDetailsService, persistentTokenRepository());
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		tokenRepositoryImpl.setDataSource(dataSource);
		return tokenRepositoryImpl;
	}

	@Bean
	public SpringSocialConfigurer springSocialConfigurer() {
		SpringSocialConfigurer config = new SpringSocialConfigurer();
		config.alwaysUsePostLoginUrl(true);
		config.postLoginUrl("/users/me");
		return config;
	}
}
