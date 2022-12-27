package paperplane.paperplane.domain.post.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paperplane.paperplane.domain.Interest.dto.InterestResponseDto;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.dto.GroupRequestDto;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;
import paperplane.paperplane.domain.post.PostColor;
import paperplane.paperplane.domain.post.dto.PostRequestDto;
import paperplane.paperplane.domain.post.dto.PostResponseDto;
import paperplane.paperplane.domain.post.repository.PostRepository;
import paperplane.paperplane.domain.post.service.PostService;
import paperplane.paperplane.domain.postinterest.dto.PostInterestResponseDto;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"Post Controller"})
@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    @ApiOperation("편지 송신/회신, 헤더에 송신자(sender) userId 필요")
    @PostMapping("/create")
    public ResponseEntity<Integer> createPost(@Valid PostRequestDto.Create create) throws Exception {
        return ResponseEntity.ok(postService.addPost(create));
    }

    @ApiOperation("편지 삭제")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable final Integer id ) throws Exception {
        postService.removePost(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("전체 편지 중 제목, 내용으로 검색")
    @GetMapping("/search/{word}")
    public ResponseEntity<List<PostResponseDto.Simple>> searchAllPost(@PathVariable final String word) throws Exception {
        List<PostResponseDto.Simple> simpleList = new ArrayList<>();
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title1")
                .content("content1")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());

        return ResponseEntity.ok(simpleList);
    }

    @ApiOperation("유저의 그룹 편지 중 제목, 내용으로 검색 user id/검색어 필요")
    @GetMapping("/search/{userid}/{word}")
    public ResponseEntity<List<PostResponseDto.Simple>> searchGroupPost(@PathVariable final Integer userid, final String word) throws Exception {
        List<PostResponseDto.Simple> simpleList = new ArrayList<>();
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title1")
                .content("content1")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());

        return ResponseEntity.ok(simpleList);
    }

    @ApiOperation("인기편지 리스트 8개 전송")
    @GetMapping("/popular")
    public ResponseEntity<List<PostResponseDto.Simple>> popularPost() throws Exception {
        List<PostResponseDto.Simple> simpleList = new ArrayList<>();
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title1")
                .content("content1")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());

        return ResponseEntity.ok(simpleList);
    }

    @ApiOperation("보낸 편지")
    @GetMapping("/sent")
    public ResponseEntity<List<PostResponseDto.Simple>> sentPost(Pageable pageable) throws Exception {
        List<PostResponseDto.Simple> simpleList = new ArrayList<>();
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title1")
                .content("content1")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());

        return ResponseEntity.ok(simpleList);
    }
    @ApiOperation("받은 편지")
    @GetMapping("/received")
    public ResponseEntity<List<PostResponseDto.Simple>> receivedPost(Pageable pageable) throws Exception {
        List<PostResponseDto.Simple> simpleList = new ArrayList<>();
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title1")
                .content("content1")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());

        return ResponseEntity.ok(simpleList);
    }
    @ApiOperation("좋아요 누른 편지")
    @GetMapping("/liked")
    public ResponseEntity<List<PostResponseDto.Simple>> likedPost(Pageable pageable) throws Exception {
        List<PostResponseDto.Simple> simpleList = new ArrayList<>();
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title1")
                .content("content1")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
                .postColor(PostColor.RED)
                .build());

        return ResponseEntity.ok(simpleList);
    }

    @ApiOperation("편지 신고")
    @GetMapping("/report/{postId}")
    public ResponseEntity<Void> reportPost(@PathVariable Integer postId) throws Exception {
        return ResponseEntity.ok().build();
    }

    @CacheEvict(value = "post-likecount", key = "#id", cacheManager = "cacheManager")
    @ApiOperation("편지 좋아요 누르기")
    @GetMapping("/like/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Integer postId) throws Exception {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test/{id}")
    @Cacheable(value = "test", key = "#id", cacheManager = "cacheManager")
    public ResponseEntity<Integer> test(@PathVariable Integer id){
        return ResponseEntity.ok(postRepository.findById(id).get().getLikeCount());

    }
}
