package paperplane.paperplane.domain.post.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paperplane.paperplane.domain.post.PostColor;
import paperplane.paperplane.domain.post.dto.PostRequestDto;
import paperplane.paperplane.domain.post.dto.PostResponseDto;
import paperplane.paperplane.domain.post.repository.PostRepository;
import paperplane.paperplane.domain.post.service.PostService;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Post Controller"})
@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    @ApiOperation("편지 송신/회신, 헤더에 userId 필요")
    @PostMapping("/create")
    public ResponseEntity<Integer> createPost(@Valid PostRequestDto.Create create) throws Exception {
        return ResponseEntity.ok(postService.addPost(create));
    }

    @ApiOperation("편지 삭제")
    @DeleteMapping("/delete/{postid}")
    public ResponseEntity<Void> deletePost(@PathVariable final Integer id) throws Exception {
        postService.removePost(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("전체 편지 중 제목, 내용으로 검색")
    @GetMapping("/search/{word}")
    public ResponseEntity<List<PostResponseDto.Simple>> searchAllPost(@PathVariable final String word, @RequestParam("page") Integer page) throws Exception {
        PageRequest pageRequest= PageRequest.of(page,8);
        return ResponseEntity.ok(postService.SearchPostByWord(word,pageRequest));
    }
    @ApiOperation("보낸 편지, 헤더에 userId 필요")
    @GetMapping("/sent")
    public ResponseEntity<List<PostResponseDto.Simple>> sentPost(@RequestParam("page") Integer page) throws Exception {
        PageRequest pageRequest= PageRequest.of(page,8);
        return ResponseEntity.ok(postService.getSentPost(pageRequest));
    }


    @ApiOperation("인기편지 리스트 8개 전송")
    @GetMapping("/popular")
    public ResponseEntity<List<PostResponseDto.Simple>> popularPost() throws Exception {
        List<PostResponseDto.Simple> simpleList= postService.getPopularPost();
        return ResponseEntity.ok(postService.getPopularPost());
    }


    @ApiOperation("받은 편지, 헤더에 userId 필요")
    @GetMapping("/received")
    public ResponseEntity<List<PostResponseDto.Simple>> receivedPost(@RequestParam("page") Integer page) throws Exception {
        PageRequest pageRequest= PageRequest.of(page,8);
        return ResponseEntity.ok(postService.getReceivedPost(pageRequest));
    }
    @ApiOperation("좋아요 누른 편지, 헤더에 userId 필요")
    @GetMapping("/liked")
    public ResponseEntity<List<PostResponseDto.Simple>> likedPostList(@RequestParam("page") Integer page) throws Exception {
        PageRequest pageRequest= PageRequest.of(page,8);
        return ResponseEntity.ok(postService.getLikedPost(pageRequest));
    }

    @ApiOperation("편지 신고")
    @GetMapping("/report/{postId}")
    public ResponseEntity<Integer> reportPost(@PathVariable Integer postId) throws Exception {
        return ResponseEntity.ok(postService.increasePostReportCount(postId)) ;
    }

    @ApiOperation("편지 좋아요 개수 새로고침")
    @GetMapping("/like/refresh/{postId}")
    public ResponseEntity<Integer> refreshPostLikeCount(@PathVariable Integer postId) throws Exception {
        return ResponseEntity.ok(postService.getLikeCountByPostId(postId));
    }

    @ApiOperation("편지 좋아요 누르기, 헤더에 userId 필요")
    @GetMapping("/like/push/{postId}")
    public ResponseEntity<Integer> likePost(@PathVariable Integer postId) throws Exception {
        return ResponseEntity.ok(postService.increasePostLikeCount(postId));
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
}
