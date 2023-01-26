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

    @Query(value = "select up from  UserPost  up join Post p on up.post.id=p.id where (p.id=:postId and p.sender.id=:userId)")
    List<UserPost> findPostOptionByPostId(@Param("userId") Integer userId, @Param("postId") Integer postId);

    @Query(value = "select count(*)from  UserPost  up join Post p on up.post=p where (up.replyId=:originId and p.sender.id=:userId) group by up.replyId,p.sender.id")
    Integer countUserPostBySenderIdAndOriginId(@Param("userId") Integer userId, @Param("originId") Integer originId);

    Optional<UserPost> findByReceiverIdAndPostId(Integer receiverId,Integer postId);
}
