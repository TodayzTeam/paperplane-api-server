package paperplane.paperplane.domain.post.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import paperplane.paperplane.domain.post.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT * FROM Post p where((p.content like CONCAT('%',:word,'%')) or (p.title like CONCAT('%',:word,'%'))) ",
            countQuery = "SELECT * FROM Post p where(p.content like CONCAT('%',:word,'%')or (p.title like CONCAT('%',:word,'%')))",
            nativeQuery = true)
    Page<Post> findAllByWord(@Param("word") String word, Pageable pageable);

    List<Post> findTop8ByOrderByLikeCountCountDesc();

    @Query(value = "select * FROM Post p where p.sender.id =:id",
    countQuery = "select * FROM Post p where p.sender.id =:id",
    nativeQuery = true)
    Page<Post> findSentPost(@Param("id")Integer id,Pageable pageable);
}
