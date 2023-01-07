package paperplane.paperplane.domain.userinterest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paperplane.paperplane.domain.userinterest.UserInterest;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest,Integer> {

}
