package pl.edu.agh.to.cinemanager.repository.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.Review;

public class MovieSpecification {
    public static Specification<Movie> genre(String genreName) {
        return (root, query, builder) -> {
            if (genreName == null || genreName.isEmpty()) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.equal(root.get("genre").get("name"), genreName);
        };
    }

    public static Specification<Movie> title(String title) {
        return (root, query, builder) -> {
            if (title == null) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Movie> minRating(Double minRating) {
        return (root, query, builder) -> {
            if (minRating == null) {
                return builder.isTrue(builder.literal(true));
            }

            Subquery<Double> subquery = query.subquery(Double.class);
            Root<Review> reviewRoot = subquery.from(Review.class);

            subquery.select(builder.avg(reviewRoot.get("rating")))
                    .where(builder.equal(reviewRoot.get("movie"), root));

            Expression<Double> averageRating = builder.coalesce(subquery, 0.0);

            return builder.greaterThanOrEqualTo(averageRating, minRating);
        };
    }
}
