package uni.social.connect.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import uni.social.connect.Application;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity(name = "verification_token")
@Table(name = "verification_token")
public class VerificationToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "token")
	private String token;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	@NotNull
	@Column(name = "expiry_date", nullable = false)
	private LocalDateTime expiryDate;

	@Column(name = "used")
	private Boolean used = false;

	public VerificationToken() {
		super();
	}

	public VerificationToken(String token, User user) {
		super();
		this.token = token;
		this.user = user;
		this.expiryDate = calculateExpiryDate(Application.Constants.VERIFICATION_TOKEN_EXPIRATION);
		this.used = false;
	}

	private static LocalDateTime calculateExpiryDate(int expiryTimeInMinutes) {
		Duration duration = Duration.ofMinutes(expiryTimeInMinutes);
		LocalDateTime time = LocalDateTime.now();
		return time.plus(duration);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}
}
