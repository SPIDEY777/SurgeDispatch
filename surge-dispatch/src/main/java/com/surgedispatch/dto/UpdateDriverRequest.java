package com.surgedispatch.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateDriverRequest {

    @Pattern(
            regexp = ".*\\S.*",
            message = "Name cannot be blank"
    )
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^\\+[1-9]\\d{1,14}$",
            message = "Invalid phone number format"
    )
    private String phone;

    @Pattern(
            regexp = ".*\\S.*",
            message = "License number cannot be blank"
    )
    private String licenseNumber;

    @AssertTrue(message = "At least one field must be provided for update")
    public boolean hasUpdateFields() {
        return name != null
                || email != null
                || phone != null
                || licenseNumber != null;
    }
}