package com.prs.rs.controller;

import com.prs.rs.dto.request.ReviewEditDto;
import com.prs.rs.dto.request.ReviewListDto;
import com.prs.rs.dto.request.ReviewWriteDto;
import com.prs.rs.dto.response.ReviewListResultDto;
import com.prs.rs.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "리뷰 API", description = "리뷰에 대한 조회, 생성, 삭제 API")
public class ReviewController {

    private final ReviewService reviewService;
    
    @PostMapping("/review")
    @Operation(summary = "리뷰 작성")
    public String writeReview(@RequestBody @Valid ReviewWriteDto reviewWriteDto) {
        reviewService.addReview(reviewWriteDto);
        return "ok";
    }

    @PatchMapping("/review")
    @Operation(summary = "리뷰 수정")
    public String editReview(@RequestBody @Valid ReviewEditDto reviewEditDto) {
        reviewService.updateReview(reviewEditDto);
        return "ok";
    }

    @DeleteMapping("/review")
    @Operation(summary = "리뷰 삭제")
    public String removeReview(@RequestParam(name = "id") Long id) {
        reviewService.deleteReview(id);
        return "ok";
    }

    @GetMapping("/review")
    @Operation(summary = "리뷰 목록")
    public ReviewListResultDto listReview(@ModelAttribute @Valid ReviewListDto reviewListDto) {
        return reviewService.getReviewList(reviewListDto);
    }
}
