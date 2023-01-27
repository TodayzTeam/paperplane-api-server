package paperplane.paperplane.domain.post.repository;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.models.auth.In;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
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

    @Query(value = "select p from Post p where p.sender.id = :userId and p.group is null order by p.date desc")
    Page<Post> findSentRandomPost(@org.springframework.data.repository.query.Param("userId")Integer userId,Pageable pageable);

    @Query(value = "select p from Post p where p.sender.id = :userId and p.group is not null order by p.date desc")
    Page<Post> findSentGroupPost(@org.springframework.data.repository.query.Param("userId")Integer userId,Pageable pageable);

    @Query(value = "select p from Post p inner join p.userPosts up on (up.receiver.id=:userId and up.isReport=false)")
    Page<Post> findReceivedPost(@Param("userId") Integer userId, Pageable pageable);

    @Query(value = "select p from Post p join p.userPosts up on (up.receiver.id = :userId and  p.sender.id <> :userId and up.isReport = false and up.isRead = true) order by p.date desc")
    Page<Post> findReceivedReadPost(@org.springframework.data.repository.query.Param("userId") Integer userId, Pageable pageable);

    @Query(value = "select p from Post p join p.userPosts up on (up.receiver.id = :userId and p.sender.id <> :userId and up.isReport = false and up.isRead = false) order by p.date desc" )
    Page<Post> findReceivedUnreadPost(@org.springframework.data.repository.query.Param("userId") Integer userId, Pageable pageable);

    @Query(value = "select p from Post p inner join p.userPosts up on (up.receiver.id=:userId and up.isReport=false AND up.isLike=true ) order by p.date desc")
    Page<Post> findLikedPost(@Param("userId") Integer userId, Pageable pageable);

    @Query(value = "select distinct p from Post p join p.userPosts up on (up.receiver.id = :userId and up.isReport = false) where p.group.id = :groupId order by p.date desc")
    List<Post> findGroupPost(@org.springframework.data.repository.query.Param("groupId") Integer groupId,
                             @org.springframework.data.repository.query.Param("userId") Integer userId);

    @Query(value = "select distinct p from Post p join p.userPosts up on (up.receiver.id = :userId and up.isReport = false) " +
            "where p.group.id = :groupId and ((p.content like CONCAT('%',:word,'%')) or (p.title like CONCAT('%',:word,'%')) ) order by p.date desc")
    List<Post> findGroupPostByWord(@Param("groupId")Integer groupId, @Param("userId") Integer userId, @Param("word") String word);
}
