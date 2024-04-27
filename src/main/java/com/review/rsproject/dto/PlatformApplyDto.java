package com.review.rsproject.dto;

import com.review.rsproject.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformApplyDto {

    private String name;
    private String url;
    private String description;

}
