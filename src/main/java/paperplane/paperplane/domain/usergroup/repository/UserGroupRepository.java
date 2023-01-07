package paperplane.paperplane.domain.usergroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import paperplane.paperplane.domain.usergroup.UserGroup;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {

    @Query("select ug from UserGroup ug join fetch ug.group join ug.user ugu where ugu.id = :userId")
    List<UserGroup> findByUserId(@Param("userId") Integer userId);

    @Query("select ug from UserGroup ug join fetch ug.user join ug.group ugg where ugg.name = :name")
    List<UserGroup> findByGroupName(@Param("name") String name);

    @Query("select ug from UserGroup ug join ug.group ugg join ug.user ugu where ugg.code = :code and ugu.email = :email")
    Optional<UserGroup> findByGroupCodeAndUserEmail(@Param("code") String code, @Param("email") String email);
}
