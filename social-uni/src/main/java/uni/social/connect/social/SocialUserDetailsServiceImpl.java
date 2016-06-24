package uni.social.connect.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import uni.social.connect.dao.UserRepository;
import uni.social.connect.model.User;
import uni.social.connect.service.UserDetailsServiceImpl;

@Service
public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		User user = userRepository.findOneByUsername(userId);

		if (user == null) {
			throw new UsernameNotFoundException(userId + " not found!");
		}

		// auditService.auditRequest(AuditRecord.RequestType.USER_SUCCESSFUL_LOGIN);
		return new UserDetailsServiceImpl.Account(user);
	}

}
