package com.review.rsproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlatformSearchDto {


    static class dto {
        private String name;
        private Integer star;
        private Integer reviewCount;
    }

}
