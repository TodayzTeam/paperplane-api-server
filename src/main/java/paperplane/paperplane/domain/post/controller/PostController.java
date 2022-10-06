package paperplane.paperplane.domain.post.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paperplane.paperplane.domain.Interest.dto.InterestResponseDto;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.dto.GroupRequestDto;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;
import paperplane.paperplane.domain.post.dto.PostRequestDto;
import paperplane.paperplane.domain.post.dto.PostResponseDto;
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
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime date;
    private Integer reportCount;
    private Integer likeCount;
    private GroupResponseDto.Info group;
    private List<PostInterestResponseDto> interest;
    @ApiOperation("편지 송신/회신")
    @PostMapping("/create")
    public ResponseEntity<PostResponseDto.Info> createPost(@Valid PostRequestDto.Create create) throws Exception {
        List<PostInterestResponseDto> postInterestResponseDtoList= new ArrayList<>();
        postInterestResponseDtoList.add(PostInterestResponseDto.builder()
                .id(1)
                .keyword("관심사1")
                .build());
        postInterestResponseDtoList.add(PostInterestResponseDto.builder()
                .id(2)
                .keyword("관심사2")
                .build());
        return ResponseEntity.ok(PostResponseDto.Info.builder()
                .id(1)
                .title("title")
                .content("content")
                .date(LocalDateTime.now())
                .reportCount(0)
                .likeCount(0)
                .group(GroupResponseDto.Info.of(Group.builder()
                        .id(1)
                        .code("12dsa")
                        .name("wwe")
                        .build()))
                .interest(postInterestResponseDtoList)
                .build());
    }

    @ApiOperation("편지 삭제")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable final Integer id ) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("편지 제목으로 검색")
    @GetMapping("/search/{title}")
    public ResponseEntity<List<PostResponseDto.Simple>> searchPost(@PathVariable final String title) throws Exception {
        List<PostResponseDto.Simple> simpleList = new ArrayList<>();
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title1")
                .content("content1")
                .likeCount(0)
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
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
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
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
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
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
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
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
                .build());
        simpleList.add(PostResponseDto.Simple.builder()
                .title("title2")
                .content("content2")
                .likeCount(0)
                .build());

        return ResponseEntity.ok(simpleList);
    }

    @ApiOperation("편지 신고")
    @GetMapping("/report/{postId}")
    public ResponseEntity<Void> reportPost(@PathVariable Integer postId) throws Exception {
        return ResponseEntity.ok().build();
    }
    @ApiOperation("편지 좋아요 누르기")
    @GetMapping("/like/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Integer postId) throws Exception {
        return ResponseEntity.ok().build();
    }


}
