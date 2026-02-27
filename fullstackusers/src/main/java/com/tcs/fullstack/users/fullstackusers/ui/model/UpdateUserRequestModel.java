package com.tcs.fullstack.users.fullstackusers.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserRequestModel {
    @NotBlank(message="First name cannot be null")
    @Size(min=2, message="First name must not be less that 2 characters")
    //@Pattern(regexp= "[a-z]+", flags={Pattern.Flag.CASE_INSENSITIVE}, message="First name must not use any special characters.")
    private String firstName;

    @NotBlank(message="Last name cannot be null")
    @Size(min=2, message="Last name must not be less that 2 characters")
    //@Pattern(regexp= "[a-z]+", flags={Pattern.Flag.CASE_INSENSITIVE}, message="Last name must not use any special characters.")
    private String lastName;

    @NotBlank(message="Email cannot be null")
    @Email
    private String email;

    @NotBlank(message="UserId cannot be null")
    private String userId;

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
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
