package paperplane.paperplane.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperplane.paperplane.domain.post.Post;

public interface UserRepository extends JpaRepository<Post, Integer> {
}
