package paperplane.paperplane.domain.group.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> findByCode(String code);

    List<Group> findByNameContaining(String name);

    boolean existsByName(String name);

    @Query(value = "select gu.user from Group g join g.userGroups gu where g.name = :name")
    Page<User> getGroupMemberListByName(@org.springframework.data.repository.query.Param("name") String name, Pageable pageable);

    @Query(value = "select User FROM Group g where g.code = :code", nativeQuery = true)
    public List<User> findGroupUserByCode(@Param("code") String code);
}
