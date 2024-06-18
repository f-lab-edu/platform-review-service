package com.prs.rs.dto.request;

import com.prs.rs.type.SortType;
import com.prs.rs.validator.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Schema(description = "리뷰 목록 DTO")
public class ReviewListDto {

    @NotNull
    @Schema(description = "리뷰를 조회할 플랫폼 ID")
    private Long platformId;

    @NotNull
    @Schema(description = "페이지 번호")
    private Integer page;

    @ValidEnum(enumClass = SortType.class)
    @Schema(description = "정렬 옵션")
    private SortType sort;


    public ReviewListDto() {
        this.sort = SortType.DATE_DESC;
        this.page = 0;
    }
}
