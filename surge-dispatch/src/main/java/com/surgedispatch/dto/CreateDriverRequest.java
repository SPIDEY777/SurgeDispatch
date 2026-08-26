package com.surgedispatch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDriverRequest {

    @NotBlank
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
    private String phone;

    @Email
    @NotBlank
    private String email;

    @NotBlank(message = "License number is required")
    private String licenseNumber;
}