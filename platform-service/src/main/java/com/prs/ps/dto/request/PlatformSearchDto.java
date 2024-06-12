package com.prs.ps.dto.request;



import com.prs.ps.type.SortType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Schema(description = "플랫폼 검색 DTO")
public class PlatformSearchDto {


    @NotBlank
    @Schema(description = "검색할 플랫폼 이름")
    private String query;

    @Schema(description = "페이지 번호", defaultValue = "0")
    private Integer page;

    @Schema(description = "정렬 옵션", defaultValue = "DATE_ASC")
    private SortType sort;

    public PlatformSearchDto() {

        this.page = 0;
        this.sort = SortType.DATE_ASC;

    }
}
