package com.prs.ms.dto;


import com.prs.ms.type.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponseDto {

    private Long memberId;
    private String name;

}
