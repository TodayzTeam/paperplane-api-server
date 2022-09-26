package paperplane.paperplane.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperplane.paperplane.domain.group.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {
}
