package com.review.rsproject.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWriteDto {

    @NotNull
    private Long platformId;

    @Length(max = 500)
    private String content;

    @NotNull
    private Byte star;

}
