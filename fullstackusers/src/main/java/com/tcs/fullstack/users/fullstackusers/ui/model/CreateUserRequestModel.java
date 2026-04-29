package com.tcs.fullstack.users.fullstackusers.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * A model class for making the User Request with appropriate getters and
 * setters. Each variable will need to pass its respective validity check before
 * a new user can be create into the database.
 */
public class CreateUserRequestModel {

    @NotBlank(message = "First name cannot be null")
    @Size(min = 2, message = "First name must not be less that 2 characters")
    // @Pattern(regexp= "[a-z]+", flags={Pattern.Flag.CASE_INSENSITIVE},
    // message="First name must not use any special characters.")
    private String firstName;

    @NotBlank(message = "Last name cannot be null")
    @Size(min = 2, message = "Last name must not be less that 2 characters")
    // @Pattern(regexp= "[a-z]+", flags={Pattern.Flag.CASE_INSENSITIVE},
    // message="Last name must not use any special characters.")
    private String lastName;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 6, message = "Password must not be less that 6 characters")
    // @Pattern(regexp=
    // "^(?=.*[a-z]+)(?=.*[A-Z]+)(?=.*[0-9]+)(?=.*[!@#$%^&*=+?]+).*$",
    // message="Password must have at least 1 lowercase letter, 1 upper case letter,
    // 1 number, and 1 special character")
    private String password;

    @NotBlank(message = "Email cannot be null")
    @Email
    private String email;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
