package paperplane.paperplane.domain.userpost.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.userpost.UserPost;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost,Integer> {
    @Query(value = "select up from UserPost up where  (up.post.id=:postId and up.receiver.id=:userId)")
    Optional< UserPost> findByReceiverIdAndPostId(@Param("userId")Integer userId, @Param("postId") Integer postId);

    //@Query(value = "select up from UserPost up left join Post p on p.id=:postId where p.sender.id=:userId")

    @Query(value = "select * from UserPost up join (select p.id from Post p where p.sender_id=:userId and p.id=:postId) ps",
    nativeQuery = true)
    List<UserPost> findPostOptionByPostId(@Param("userId") Integer userId, @Param("postId") Integer postId);
    @Query(value = "select * from Post p where p.sender_id=:userId and p.id=:postId",nativeQuery = true)
    List<Post> test(@Param("userId") Integer userId, @Param("postId") Integer postId);
}
