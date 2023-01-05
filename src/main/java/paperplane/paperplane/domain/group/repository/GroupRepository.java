package paperplane.paperplane.domain.group.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    public Optional<Group> findByCode(String code);

    @Query(value = "select User FROM Group g where g.code = :code", nativeQuery = true)
    public List<User> findGroupUserByCode(@Param("code") String code);
}
