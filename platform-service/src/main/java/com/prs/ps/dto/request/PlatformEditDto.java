package com.prs.ps.dto.request;


import com.prs.ps.validator.ValidEnum;
import com.prs.ps.type.PlatformStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "플랫폼 수정 DTO")
public class PlatformEditDto {


    @NotNull
    @Schema(description = "플랫폼 아이디")
    private Long platformId;

    @Length(min = 10, max = 50)
    @Schema(description = "플랫폼 설명")
    private String description;

    @ValidEnum(enumClass = PlatformStatus.class)
    @Schema(description = "플랫폼 상태")
    private PlatformStatus status;

}
