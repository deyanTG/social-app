package uni.social.connect.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import uni.social.connect.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	PasswordResetToken findByToken(String token);
}
