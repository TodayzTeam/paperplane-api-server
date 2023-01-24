package paperplane.paperplane.domain.post.repository;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.user.User;


import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT * FROM Post p where((p.content like CONCAT('%',:word,'%')) or (p.title like CONCAT('%',:word,'%'))) ",
            countQuery = "SELECT * FROM Post p where(p.content like CONCAT('%',:word,'%')or (p.title like CONCAT('%',:word,'%')))",
            nativeQuery = true)
    Page<Post> findAllByWord(@Param("word") String word, Pageable pageable);
    List<Post> findTop8ByOrderByLikeCountDesc();

    void deleteByGroup_Id(Integer groupId);

    @Query(value = "select p from Post p inner join p.sender  s on s.id=:userId inner join p.userPosts up on up.isReport=false ")
    Page<Post> findSentPost(@Param("userId")Integer userId,Pageable pageable);


    @Query(value = "select p from Post p inner join p.userPosts up on (up.receiver.id=:userId and up.isReport=false)")
    Page<Post> findReceivedPost(@Param("userId") Integer userId, Pageable pageable);

    @Query(value = "select p from Post p join p.userPosts up on (up.receiver.id = :userId and up.isReport = false and up.isRead = true)")
    Page<Post> findReceivedReadPost(@org.springframework.data.repository.query.Param("userId") Integer userId, Pageable pageable);

    @Query(value = "select p from Post p join p.userPosts up on (up.receiver.id = :userId and up.isReport = false and up.isRead = false)")
    Page<Post> findReceivedUnreadPost(@org.springframework.data.repository.query.Param("userId") Integer userId, Pageable pageable);

    @Query(value = "select p from Post p inner join p.userPosts up on (up.receiver.id=:userId and up.isReport=false AND up.isLike=true )")
    Page<Post> findLikedPost(@Param("userId") Integer userId, Pageable pageable);

    @Query(value = "select * from Post p where p.sender_id=:userId and p.id=:postId",nativeQuery = true)
    List<Post> test(@Param("userId") Integer userId, @Param("postId") Integer postId);

    @Query(value = "select p from Post p join p.userPosts up on up.isReport = false where p.group.id = :groupId and p.date >= :joinDate ")
    List<Post> findGroupPost(@org.springframework.data.repository.query.Param("groupId") Integer groupId, @org.springframework.data.repository.query.Param("joinDate")LocalDateTime joinDate);

    @Query(value = "SELECT p FROM Post p inner join Group g on g.id=:groupId inner join p.userPosts up on up.isReport=false where((p.content like CONCAT('%',:word,'%')) or (p.title like CONCAT('%',:word,'%')) ) ")
    Page<Post> findGroupPostByWord(@Param("groupId")Integer groupId,@Param("word") String word, Pageable pageable);
}
