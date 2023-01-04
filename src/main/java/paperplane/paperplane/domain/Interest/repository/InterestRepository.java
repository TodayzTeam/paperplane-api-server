package paperplane.paperplane.domain.Interest.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import paperplane.paperplane.domain.Interest.Interest;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    Optional<Interest> findByKeyword(String keyword);

    @Query(value = "SELECT * FROM Interest i where i.keyword like CONCAT('%',:keyword,'%') ",
            countQuery = "SELECT * FROM Interest i where i.keyword like CONCAT('%',:keyword,'%') ",
            nativeQuery = true)
    List<Interest> findAllByKeyword(@Param("keyword") String keyword);

    @Query(value = "select i from Interest i join UserInterest ui on ui.user.id=:id ")
    List<Interest> findMyInterest(@Param("id") Integer id);

    List<Interest> findTop8ByOrderByCountDesc();


}
