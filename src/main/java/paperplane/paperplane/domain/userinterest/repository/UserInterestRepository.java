package paperplane.paperplane.domain.userinterest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import paperplane.paperplane.domain.userinterest.UserInterest;

import java.util.Optional;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest,Integer> {

      @Query(value = "select ui from UserInterest ui where ui.user.id = :userId and ui.interest.id = :interestId")
      Optional<UserInterest> findByUserIdAndKeyword(@Param("userId") Integer userId, @Param("interestId") Integer interestId);

}
