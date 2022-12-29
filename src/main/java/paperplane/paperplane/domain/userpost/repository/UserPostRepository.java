package paperplane.paperplane.domain.userpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paperplane.paperplane.domain.userpost.UserPost;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost,Integer> {
}
