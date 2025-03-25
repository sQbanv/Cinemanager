package pl.edu.agh.to.cinemanager.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.Review;
import pl.edu.agh.to.cinemanager.model.User;

public class ReviewSpecification {
    public static Specification<Review> user(User user) {
        return (root, query, builder) -> {
            if (user == null) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.equal(root.get("user"), user);
        };
    }

    public static Specification<Review> movie(Movie movie) {
        return (root, query, builder) -> {
            if (movie == null) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.equal(root.get("movie"), movie);
        };
    }
}
