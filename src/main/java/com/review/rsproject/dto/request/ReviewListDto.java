package com.review.rsproject.dto.request;


import com.review.rsproject.type.SortType;
import com.review.rsproject.validator.ValidEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ReviewListDto {

    @NotNull
    private Long id;

    @NotNull
    private Integer page;

    @ValidEnum(enumClass = SortType.class)
    private SortType sort;


    public ReviewListDto()
    {
        this.sort = SortType.DATE_DESC;
        this.page = 0;
    }
}
