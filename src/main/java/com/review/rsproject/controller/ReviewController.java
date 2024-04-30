package com.review.rsproject.controller;

import com.review.rsproject.dto.request.ReviewWriteDto;
import com.review.rsproject.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
