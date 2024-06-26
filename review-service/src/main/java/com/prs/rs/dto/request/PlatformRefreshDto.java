package com.prs.rs.dto.request;


import com.prs.rs.type.ActionStatus;
import java.util.UUID;
import lombok.Data;

@Data
public class PlatformRefreshDto {


    private String messageId;
    private Long platformId;
    private ActionStatus action;
    private Integer score;
    private Integer beforeScore;


    public PlatformRefreshDto(Long platformId, ActionStatus action, Integer score) {

        this.messageId = UUID.randomUUID().toString();
        this.platformId = platformId;
        this.action = action;
        this.score = score;
        this.beforeScore = null;
    }

    public PlatformRefreshDto(Long platformId, ActionStatus action, Integer afterScore,
        Integer beforeScore) {
        this(platformId, action, afterScore);
        this.beforeScore = beforeScore;
    }



}
