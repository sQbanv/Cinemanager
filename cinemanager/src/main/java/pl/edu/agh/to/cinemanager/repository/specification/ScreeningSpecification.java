package pl.edu.agh.to.cinemanager.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.Screening;

import java.time.LocalDateTime;

public class ScreeningSpecification {
    public static Specification<Screening> movie(Movie movie) {
        return (root, query, builder) -> {
            if (movie == null) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.equal(root.get("movie"), movie);
        };
    }

    public static Specification<Screening> afterDate(LocalDateTime after) {
        return (root, query, builder) -> {
            if (after == null) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.greaterThanOrEqualTo(root.get("startDate"), after);
        };
    }
}
