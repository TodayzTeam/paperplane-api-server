package paperplane.paperplane.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperplane.paperplane.domain.post.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
