package com.prs.ps.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "플랫폼 등록 신청 DTO")
public class PlatformApplyDto {

    @Length(min = 1, max = 20)
    @Schema(description = "플랫폼 이름", defaultValue = "네이버")
    private String name;

    @Pattern(regexp = "^https:\\/\\/.*$")
    @Schema(description = "플랫폼 URL", defaultValue = "https://naver.com")
    private String url;

    @Length(min = 10, max = 50)
    @Schema(description = "플랫폼에 대한 간단한 설명", defaultValue = "검색 엔진 플랫폼입니다.")
    private String description;

}

