package com.review.rsproject.dto.request;


import com.review.rsproject.type.SortType;
import com.review.rsproject.validator.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Schema(description = "리뷰 목록 DTO")
public class ReviewListDto {

    @NotNull
    @Schema(description = "리뷰를 조회할 플랫폼 ID")
    private Long id;

    @NotNull
    @Schema(description = "페이지 번호", defaultValue = "0")
    private Integer page;

    @ValidEnum(enumClass = SortType.class)
    @Schema(description = "정렬 옵션", defaultValue = "DATE_DCES")
    private SortType sort;


    public ReviewListDto()
    {
        this.sort = SortType.DATE_DESC;
        this.page = 0;
    }
}
