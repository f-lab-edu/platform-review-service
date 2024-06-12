package com.prs.ms.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "회원가입 DTO")
public class MemberRegisterDto {


    @Length(min = 7, max = 15)
    @Pattern(regexp = "^[a-z0-9]+$")
    @Schema(description = "7자 이상 15자 이하의 영문 소문자와 숫자가 포함되어야 합니다.", defaultValue = "admin123123")
    private String username;

    @Length(min = 8, max = 15)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9!@#$*%]+$")
    @Schema(description = "8자 이상 15자 이하의 영문 대/소문자, 숫자, 특수문가 포함될 수 있습니다.", defaultValue = "as33as23*!")
    private String password;
}
