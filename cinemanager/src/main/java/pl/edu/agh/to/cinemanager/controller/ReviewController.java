package pl.edu.agh.to.cinemanager.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.to.cinemanager.dto.RequestReviewDto;
import pl.edu.agh.to.cinemanager.dto.ResponseReviewDto;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.Review;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.repository.specification.ReviewSpecification;
import pl.edu.agh.to.cinemanager.service.ReviewService;

import java.net.URI;

@RestController
@RequestMapping(path = "api/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("")
    public Page<ResponseReviewDto> getAllReviews(Pageable pageable,
                                                 @RequestParam(value = "movieId", required = false) Movie movie,
                                                 @RequestParam(value = "userId", required = false) User user) {
        Specification<Review> reviewSpecification = ReviewSpecification.movie(movie)
                .and(ReviewSpecification.user(user));

        return reviewService.getAllReviews(reviewSpecification, pageable);
    }

    @GetMapping("{id}")
    public ResponseReviewDto getReviewById(@PathVariable("id") Review review) {
        return reviewService.reviewToResponseDto(review);
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseReviewDto> createReview(Authentication authentication, @RequestBody RequestReviewDto reviewDto) {
        ResponseReviewDto responseReviewDto = reviewService.createReviewByEmail(authentication.getName(), reviewDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/reviews/{id}")
                .buildAndExpand(responseReviewDto.id()).toUri();

        return ResponseEntity.created(location).body(responseReviewDto);
    }

    @PutMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReview(@PathVariable("id") Review review, @RequestBody RequestReviewDto reviewDto, Authentication authentication) {
        reviewService.updateReview(authentication, review, reviewDto);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable("id") Review review, Authentication authentication) {
        reviewService.deleteReview(authentication, review);
    }
}
