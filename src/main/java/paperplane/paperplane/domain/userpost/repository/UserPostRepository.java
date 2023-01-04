package paperplane.paperplane.domain.userpost.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import paperplane.paperplane.domain.userpost.UserPost;

import java.util.Optional;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost,Integer> {
    @Query(value = "select up from UserPost up where  (up.post.id=:postId and up.receiver.id=:userId)")
    Optional< UserPost> findByReceiverIdAndPostId(@Param("userId")Integer userId, @Param("postId") Integer postId);
}
