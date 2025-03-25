package pl.edu.agh.to.cinemanager.service;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.cinemanager.dto.*;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.Review;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.model.UserRole;
import pl.edu.agh.to.cinemanager.repository.ReviewRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final MovieService movieService;
    private final AuthService authService;

    public Page<ResponseReviewDto> getAllReviews(Specification<Review> specification, Pageable pageable) {
        return reviewRepository
                .findAll(specification, PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    pageable.getSortOr(Sort.by(Sort.Direction.DESC, "id"))))
                .map(this::reviewToResponseDto);
    }

    public ResponseReviewDto createReviewByEmail(String email, RequestReviewDto requestReviewDto) {
        User user = userService.findUserByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist")
        );

        Movie movie = movieService.getMovieById(requestReviewDto.movieId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This movie does not exist")
        );
        Review review = new Review(requestReviewDto.rating(), requestReviewDto.content(), user, movie, LocalDateTime.now());

        validateAndSave(review);

        return reviewToResponseDto(review);
    }

    public void updateReview(Authentication authentication, Review review, RequestReviewDto updatedDto) {
        User requestUser = userService.findUserByEmail(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist")
        );

        if (review.getUser().getEmail().equals(requestUser.getEmail())
                || authService.hasRole(UserRole.MANAGER, authentication.getAuthorities())) {
            Movie movie = movieService.getMovieById(updatedDto.movieId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This movie does not exist")
            );

            review.setContent(updatedDto.content());
            review.setRating(updatedDto.rating());
            review.setMovie(movie);

            validateAndSave(review);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public void deleteReview(Authentication authentication, Review review) {
        User requestUser = userService.findUserByEmail(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist")
        );

        if (review.getUser().getEmail().equals(requestUser.getEmail())
                || authService.hasRole(UserRole.MANAGER, authentication.getAuthorities())) {
            reviewRepository.delete(review);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public BigDecimal getRating(Movie movie) {
        BigDecimal rating = reviewRepository.getAverageRatingByMovie(movie);
        if (rating != null) {
            return rating.setScale(1, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.valueOf(0.0);
        }
    }

    public ResponseMovieRatingDto getHighestRatedMovies(){
        List<Object[]> data = reviewRepository.getHighestRankingMovies();
        List<MovieRatingDto> responseData = new ArrayList<>();
        for (Object[] o : data){
            Movie movie = (Movie) o[0];
            Double rating = (Double) o[1];
            BigDecimal formatted_rating = (BigDecimal.valueOf(rating)).setScale(1, RoundingMode.HALF_UP);
            responseData.add(new MovieRatingDto(movieService.movieToResponseDto(movie), formatted_rating));
        }
        return new ResponseMovieRatingDto(responseData);
    }

    private void validateAndSave(Review review) {
        try {
            if (!isValidRating(review.getRating())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Rating must be a number from 0.0 to 5.0 with a step of 0.5");
            }
            reviewRepository.save(review);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    private boolean isValidRating(BigDecimal rating) {
        BigDecimal minRating = BigDecimal.valueOf(0.0);
        BigDecimal maxRating = BigDecimal.valueOf(5.0);
        BigDecimal stepRating = BigDecimal.valueOf(0.5);

        return rating.compareTo(minRating) >= 0
                && rating.compareTo(maxRating) <= 0
                && rating.remainder(stepRating).compareTo(BigDecimal.ZERO) == 0;
    }

    public ResponseReviewDto reviewToResponseDto(Review review) {
        return new ResponseReviewDto(
                review.getId(),
                review.getMovie().getId(),
                userToResponseReviewDto(review.getUser()),
                review.getRating(),
                review.getContent(),
                review.getReviewDate()
        );
    }

    private ResponseReviewUserDto userToResponseReviewDto(User user) {
        return new ResponseReviewUserDto(user.getId(), user.getFirstName());
    }
}
