package com.review.rsproject.dto;

import com.review.rsproject.type.PlatformStatus;
import com.review.rsproject.validator.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformEditDto {


    @NotNull
    private Long id;

    @Length(min = 10, max = 50)
    private String description;

    @ValidEnum(enumClass = PlatformStatus.class)
    private PlatformStatus status;

}
