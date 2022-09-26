package paperplane.paperplane.domain.Interest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperplane.paperplane.domain.Interest.Interest;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
}
