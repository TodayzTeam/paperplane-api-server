package paperplane.paperplane.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import paperplane.paperplane.domain.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM User u where u.randId>FLOOR((RAND()*100000000)) ORDER BY u.randId limit 5", nativeQuery = true)
    List<User>findRandUserList();
}
