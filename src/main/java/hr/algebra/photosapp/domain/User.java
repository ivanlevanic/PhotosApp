package hr.algebra.photosapp.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class User {

    @Id
    @NotEmpty(message = "Username is required")
    @Size(max = 20, message = "Username should not exceed 20 characters")
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(max = 100, message = "Password should not exceed 100 characters")
    private String password;

    @NotEmpty(message = "Email is required")
    @Size(max = 100, message = "Email should not exceed 100 characters")
    private String email;

    @NotEmpty(message = "First name is required")
    @Size(max = 100, message = "First name should not exceed 100 characters")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    @Size(max = 100, message = "Last name should not exceed 100 characters")
    private String lastName;

    private Boolean enabled;

    public User() {
    }

    public User(String username, String password, String email, String firstName, String lastName, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //this.password = passwordEncoder.encode(password);
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
