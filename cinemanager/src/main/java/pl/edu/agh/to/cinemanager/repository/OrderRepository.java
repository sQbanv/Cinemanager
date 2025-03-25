package pl.edu.agh.to.cinemanager.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.cinemanager.model.Order;
import pl.edu.agh.to.cinemanager.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByPaidFalseAndCancelledFalseAndDateBefore(LocalDateTime dateTime);
}
