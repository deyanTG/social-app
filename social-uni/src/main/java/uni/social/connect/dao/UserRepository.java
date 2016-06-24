package uni.social.connect.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import uni.social.connect.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT u FROM users u WHERE u.username = :username")
	User findOneByUsername(@Param("username") String username);

	@Query("SELECT u FROM users u WHERE u.id = :id ")
	User findOneById(@Param("id") Long id);

}
