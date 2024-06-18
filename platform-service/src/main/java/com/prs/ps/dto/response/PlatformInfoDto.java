package com.prs.ps.dto.response;

import com.prs.ps.type.PlatformStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatformInfoDto {

    private String memberName;
    private String platformName;
    private String description;
    private String url;
    private PlatformStatus status;
    private LocalDateTime createdDt;
    private LocalDateTime modifiedDt;

}
