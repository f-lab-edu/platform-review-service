package com.review.rsproject.dto.request;

import com.review.rsproject.domain.Member;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformApplyDto {

    @Length(min=1, max = 20)
    private String name;

    @Pattern(regexp = "^https:\\/\\/.*$")
    private String url;

    @Length(min = 10, max = 50)
    private String description;

}
