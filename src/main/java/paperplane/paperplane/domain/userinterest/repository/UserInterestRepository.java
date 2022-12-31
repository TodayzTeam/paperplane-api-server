package paperplane.paperplane.domain.userinterest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperplane.paperplane.domain.userinterest.UserInterest;

public interface UserInterestRepository extends JpaRepository<UserInterest, Integer> {
}
