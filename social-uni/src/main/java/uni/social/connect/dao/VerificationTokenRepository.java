package uni.social.connect.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import uni.social.connect.model.User;
import uni.social.connect.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	VerificationToken findByToken(String token);

	VerificationToken findByUser(User user);
}
