package uni.social.connect.dto;

import javax.validation.constraints.NotNull;

import uni.social.connect.validation.ValidationGroups;

import java.util.HashSet;
import java.util.Set;

public class UserInput {

	@NotNull(groups = ValidationGroups.User.Update.class)
	private Long id;

	private String firstName;

	private String lastName;

	private String avatar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
