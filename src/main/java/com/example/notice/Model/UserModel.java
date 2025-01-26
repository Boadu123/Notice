package com.example.notice.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



@Entity
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "First name cannot be null or empty")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Last name cannot be null or empty")
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email
    @NotBlank(message = "Email cannot be null or empty")
    private String email;

    @Column(nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password cannot be null or empty")
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName != null ? firstName.trim() : null;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName != null ? lastName.trim() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password != null ? password.trim() : null;
    }
}
