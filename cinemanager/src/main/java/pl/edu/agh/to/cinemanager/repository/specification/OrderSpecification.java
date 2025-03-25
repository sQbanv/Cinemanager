package pl.edu.agh.to.cinemanager.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.agh.to.cinemanager.model.Order;
import pl.edu.agh.to.cinemanager.model.User;

import java.time.LocalDateTime;

public class OrderSpecification {
    public static Specification<Order> afterDate(LocalDateTime after) {
        return (root, query, builder) -> {
            if (after == null) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.greaterThanOrEqualTo(root.get("date"), after);
        };
    }

    public static Specification<Order> beforeDate(LocalDateTime before) {
        return (root, query, builder) -> {
            if (before == null) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.lessThanOrEqualTo(root.get("date"), before);
        };
    }

    public static Specification<Order> user(User user) {
        return (root, query, builder) -> {
            if (user == null) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.equal(root.get("user"), user);
        };
    }

    public static Specification<Order> paid(Boolean paid) {
        return (root, query, builder) -> {
            if (paid == null) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.equal(root.get("paid"), paid);
        };
    }

    public static Specification<Order> cancelled(Boolean cancelled) {
        return (root, query, builder) -> {
            if (cancelled == null) {
                return builder.isTrue(builder.literal(true));
            }
            return builder.equal(root.get("cancelled"), cancelled);
        };
    }
}
