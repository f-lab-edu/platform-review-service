package com.review.rsproject.dto.request;


import com.review.rsproject.type.SortType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class ReviewListDto {

    @NotNull
    private Long id;

    @NotNull
    private Integer page;

    private SortType sort;

    public ReviewListDto() {
        this.sort = SortType.DATE_DESC;
        this.page = 0;

    }
}
