package com.review.rsproject.dto;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberRegisterDto {


    @Length(min = 7, max = 15)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String username;

    @Length(min = 8, max = 15)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9!@#$*%]+$")
    private String password;
}
