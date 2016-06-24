package uni.social.connect.dto;

import uni.social.connect.model.User;


public class SimpleUserOutput implements EntityOutput{

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private User.Role role;

    private Boolean expert = false;

    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }

    public Boolean getExpert() {
        return expert;
    }

    public void setExpert(Boolean expert) {
        this.expert = expert;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
