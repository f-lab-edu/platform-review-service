package com.review.rsproject.dto.request;


import com.review.rsproject.type.PlatformSort;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlatformSearchDto {


    @NotBlank
    private String query;

    private Integer page;


    private PlatformSort sort;

    public PlatformSearchDto() {

        this.page = 0;
        this.sort = PlatformSort.DATE_ASC;

    }
}
