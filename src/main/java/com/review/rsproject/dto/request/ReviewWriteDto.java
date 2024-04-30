package com.review.rsproject.dto.request;


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
public class ReviewWriteDto {

    @NotNull
    private Long id;

    @Length(max = 500)
    @NotBlank
    private String content;

    @NotNull
    @Range(min = 1, max = 10)
    private Byte star;

}
