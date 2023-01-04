package paperplane.paperplane.domain.usergroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import paperplane.paperplane.domain.usergroup.UserGroup;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {

    @Query("select ug from UserGroup ug join fetch ug.group where ug.user.id = :userId")
    List<UserGroup> findByUserId(@Param("userId") Integer userId);

    @Query("select ug from UserGroup ug join fetch ug.user where ug.group.name = :name")
    List<UserGroup> findByGroupName(@Param("name") String name);

    @Query("select ug from UserGroup ug where ug.group.code = :code and ug.user.email = :email")
    Optional<UserGroup> findByGroupCodeAndUserEmail(@Param("code") String code, @Param("email") String email);
}
