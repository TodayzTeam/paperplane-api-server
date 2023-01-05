package paperplane.paperplane.domain.post.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.user.User;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT * FROM Post p where((p.content like CONCAT('%',:word,'%')) or (p.title like CONCAT('%',:word,'%'))) ",
            countQuery = "SELECT * FROM Post p where(p.content like CONCAT('%',:word,'%')or (p.title like CONCAT('%',:word,'%')))",
            nativeQuery = true)
    Page<Post> findAllByWord(@Param("word") String word, Pageable pageable);
    List<Post> findTop8ByOrderByLikeCountDesc();

    /*@Query(value = "select * FROM Post p left join (SELECT * from User u where u.id =:id) as u",
    countQuery = "select * FROM Post p left join (SELECT * from User u where u.id =:id) as u",
    nativeQuery = true)*/
    @Query(value = "select p from Post p join p.sender s on s.id=:id")
    Page<Post> findSentPost(@Param("id")Integer id,Pageable pageable);

    /*@Query(value = "SELECT * from Post as p join p.userPosts as up join up.receiver as r on r.id = :id",
    countQuery = "SELECT * from Post as p join p.userPosts as up join up.receiver as r on r.id = :id",
    nativeQuery = true)*/
    @Query(value = "select p from Post p join p.userPosts up on up.receiver.id=:id ")
    Page<Post> findReceivedPost(@Param("id") Integer id, Pageable pageable);

    /*@Query(value = "SELECT * from Post as p join p.userPosts as up on up.isLike=TRUE  ",
            countQuery = "SELECT * from Post as p join p.userPosts as up on up.isLike=TRUE  ",
            nativeQuery = true )*/
    @Query(value = "select p from Post p join p.userPosts up on up.isLike=true ")
    Page<Post> findLikedPost(Pageable pageable);


    @Query(value = "select * from Post p where p.sender_id=:userId and p.id=:postId",nativeQuery = true)
    List<Post> test(@Param("userId") Integer userId, @Param("postId") Integer postId);
}
