package paperplane.paperplane.domain.postinterest.repository;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paperplane.paperplane.domain.postinterest.PostInterest;

@Repository
public interface PostInterestRepository extends JpaRepository<PostInterest,Integer> {
}
