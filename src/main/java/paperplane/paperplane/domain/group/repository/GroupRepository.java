package paperplane.paperplane.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperplane.paperplane.domain.group.Group;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> findByCode(String code);

    List<Group> findByNameContaining(String name);

    boolean existsByName(String name);
}
