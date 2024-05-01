package com.review.rsproject.controller;

import com.review.rsproject.dto.request.ReviewEditDto;
import com.review.rsproject.dto.request.ReviewListDto;
import com.review.rsproject.dto.request.ReviewWriteDto;
import com.review.rsproject.dto.response.ReviewListResultDto;
import com.review.rsproject.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    
    @PostMapping("/review")
    public String writeReview(@RequestBody @Valid ReviewWriteDto reviewWriteDto) {
        reviewService.addReview(reviewWriteDto);
        return "ok";
    }

    @PatchMapping("/review")
    public String editReview(@RequestBody @Valid ReviewEditDto reviewEditDto) {
        reviewService.updateReview(reviewEditDto);
        return "ok";
    }

    @DeleteMapping("/review")
    public String removeReview(@RequestParam(name = "id") Long id) {
        reviewService.deleteReview(id);
        return "ok";
    }

    @GetMapping("/review")
    public ReviewListResultDto listReview(@ModelAttribute @Valid ReviewListDto reviewListDto) {
        return reviewService.getReviewList(reviewListDto);
    }
}
