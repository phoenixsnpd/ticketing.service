package ticketmanagement.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketmanagement.system.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}
