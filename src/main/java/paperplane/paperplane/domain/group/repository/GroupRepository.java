package paperplane.paperplane.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperplane.paperplane.domain.group.Group;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    public Optional<Group> findByCode(String code);
}
