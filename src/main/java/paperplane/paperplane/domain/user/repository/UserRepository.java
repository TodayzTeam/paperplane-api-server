package paperplane.paperplane.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import paperplane.paperplane.domain.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
