package com.review.rsproject.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "리뷰 수정 DTO")
public class ReviewEditDto {

    @NotNull
    @Schema(defaultValue = "수정할 리뷰 ID")
    private Long id;

    @NotBlank
    @Length(max = 500)
    @Schema(description = "수정할 내용")
    private String content;

    @NotNull
    @Range(min = 1, max = 10)
    @Schema(description = "수정할 별점")
    private Byte star;

}
