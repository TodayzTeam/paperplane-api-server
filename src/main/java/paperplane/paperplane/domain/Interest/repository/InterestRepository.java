package paperplane.paperplane.domain.Interest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperplane.paperplane.domain.Interest.Interest;

import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    Optional<Interest> findByKeyword(String keyword);

}
