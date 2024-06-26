package com.prs.ps.dto.response;

import com.prs.ps.type.ActionStatus;
import lombok.Data;

@Data
public class PlatformRefreshDto {

    private String messageId;
    private Long platformId;
    private ActionStatus action;
    private Integer score;
    private Integer beforeScore;

}
