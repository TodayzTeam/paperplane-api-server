package paperplane.paperplane.domain.usergroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.usergroup.UserGroup;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {

    @Query("select ug.group from UserGroup ug join ug.user ugu where ugu.id = :userId")
    List<Group> getMyGroupList(@Param("userId") Integer userId);

    @Query("select ug from UserGroup ug join ug.group ugg join ug.user ugu where ugg.code = :code and ugu.email = :email")
    Optional<UserGroup> findByGroupCodeAndUserEmail(@Param("code") String code, @Param("email") String email);
}
