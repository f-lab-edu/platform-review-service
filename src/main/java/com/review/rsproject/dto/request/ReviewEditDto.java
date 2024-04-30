package com.review.rsproject.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEditDto {

    @NotNull
    private Long id;

    @NotBlank
    @Length(max = 500)
    private String content;

    @NotNull
    @Range(min = 1, max = 10)
    private Byte star;

}
