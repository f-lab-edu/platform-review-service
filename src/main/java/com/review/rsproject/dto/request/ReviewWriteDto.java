package com.review.rsproject.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "리뷰 작성 DTO")
public class ReviewWriteDto {

    @NotNull
    @Schema(description = "리뷰 작성할 플랫폼 ID")
    private Long id;

    @Length(max = 500)
    @NotBlank
    @Schema(description = "리뷰 내용")
    private String content;

    @NotNull
    @Range(min = 1, max = 10)
    @Schema(description = "별점 1 ~ 10")
    private Byte star;

}
