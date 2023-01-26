package paperplane.paperplane.domain.post.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paperplane.paperplane.domain.post.PostColor;
import paperplane.paperplane.domain.post.dto.PostRequestDto;
import paperplane.paperplane.domain.post.dto.PostResponseDto;
import paperplane.paperplane.domain.post.service.PostService;
import paperplane.paperplane.domain.userpost.dto.UserPostRequestDto;
import paperplane.paperplane.domain.userpost.dto.UserPostResponseDto;
import paperplane.paperplane.domain.userpost.service.UserPostService;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"Post Controller"})
@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserPostService userPostService;

    @ApiOperation("편지 최종 송신")
    @PostMapping("/create")
    public ResponseEntity<Integer> createPost(@Valid PostRequestDto.Create create) throws Exception {
        return ResponseEntity.ok(postService.addPost(create));
    }

    @ApiOperation("편지 중간저장")

    @PostMapping("/save")
    public ResponseEntity<Integer> saveTempPost(@Valid PostRequestDto.Create create) throws Exception {
        return ResponseEntity.ok(postService.interStorePost(create));
    }

    @ApiOperation("편지 삭제")
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable final Integer postId) {
        postService.removePost(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("전체 편지 중 제목, 내용으로 검색")
    @GetMapping("/search/{word}")
    public ResponseEntity<List<PostResponseDto.Simple>> searchAllPost(@PathVariable final String word, @RequestParam("page") Integer page) {
        PageRequest pageRequest= PageRequest.of(page,8);
        return ResponseEntity.ok(postService.SearchPostByWord(word,pageRequest));
    }
    @ApiOperation("보낸 편지")
    @GetMapping("/sent")
    public ResponseEntity<List<PostResponseDto.Simple>> sentPost(@RequestParam("page") Integer page) {
        PageRequest pageRequest= PageRequest.of(page,8);
        return ResponseEntity.ok(postService.getSentPost(pageRequest));
    }

    @ApiOperation("인기편지 리스트 8개 전송")
    @GetMapping("/popular")
    public ResponseEntity<List<PostResponseDto.Simple>> popularPost() {
        return ResponseEntity.ok(postService.getPopularPost());
    }

    @ApiOperation("받은 편지, 헤더에")
    @GetMapping("/received")
    public ResponseEntity<List<PostResponseDto.Simple>> receivedPost(@RequestParam("page") Integer page) {
        PageRequest pageRequest= PageRequest.of(page,8);
        return ResponseEntity.ok(postService.getReceivedPost(pageRequest));
    }
    @ApiOperation("좋아요 누른 편지")
    @GetMapping("/liked")
    public ResponseEntity<List<PostResponseDto.Simple>> likedPostList(@RequestParam("page") Integer page){
        PageRequest pageRequest= PageRequest.of(page,8);
        return ResponseEntity.ok(postService.getLikedPost(pageRequest));
    }

    @ApiOperation("편지 신고, 5회 누적시 편지 삭제")
    @GetMapping("/report/{postId}")
    public ResponseEntity<Integer> reportPost(@PathVariable Integer postId) throws Exception {
        return ResponseEntity.ok(postService.increasePostReportCount(postId)) ;
    }

    @ApiOperation("편지 좋아요 개수 새로고침")
    @GetMapping("/like/refresh/{postId}")
    public ResponseEntity<Integer> refreshPostLikeCount(@PathVariable Integer postId){
        return ResponseEntity.ok(postService.getLikeCountByPostId(postId));
    }

    @ApiOperation("편지 좋아요 누르기, 헤더에")
    @GetMapping("/like/push/{postId}")
    public ResponseEntity<Integer> likePost(@PathVariable Integer postId) throws Exception {
        return ResponseEntity.ok(postService.increasePostLikeCount(postId));
    }

    @ApiOperation("편지 좋아요 취소")
    @GetMapping("/like/cancel/{postId}")
    public ResponseEntity<Integer> likeCancelPost(@PathVariable Integer postId) throws Exception {
        return ResponseEntity.ok(postService.decreasePostLikeCount(postId));
    }

    @ApiOperation("유저의 그룹 편지 중 제목, 내용으로 검색. 그룹id,검색어 필요")
    @GetMapping("/search/{groupId}/{word}")
    public ResponseEntity<List<PostResponseDto.Simple>> searchGroupPost(@PathVariable final Integer groupId,@PathVariable final String word, @RequestParam("page") Integer page) throws Exception {
        PageRequest pageRequest= PageRequest.of(page,8);
        return ResponseEntity.ok(postService.searchGroupPostByWord(groupId,word,pageRequest));
    }

    @ApiOperation("그룹 편지 리스트")
    @GetMapping("/list/{groupId}")
    public ResponseEntity<List<PostResponseDto.Simple>> getGroupPost(@PathVariable final Integer groupId) throws Exception {
        return ResponseEntity.ok(postService.getGroupPost(groupId));
    }

    @ApiOperation("편지 읽음,신고,좋아요,응답 여부 확인")
    @PostMapping("/option")
    public ResponseEntity<List<UserPostResponseDto.Option>> postOption(@Valid UserPostRequestDto userPostRequestDto) {
        return ResponseEntity.ok(userPostService.getPostOption(userPostRequestDto));
    }

    @ApiOperation("임시저장 편지 있는지 여부, 있으면 임시저장 편지 id 반환 없음 0 반환")
    @GetMapping("/temp")
    public ResponseEntity<Integer> tempPost(){
        return ResponseEntity.ok(postService.checkingTempPost());
    }
    @ApiOperation("편지 자세한 정보 postId 필요")
    @GetMapping("/info/{postId}")
    public ResponseEntity<List<PostResponseDto.Info>> postInfo(@PathVariable Integer postId) {
        return ResponseEntity.ok(postService.PostInfoById(postId));
    }
    @ApiOperation("답장편지 - 받은편지")
    @GetMapping("/reply/received")
    public ResponseEntity<List<PostResponseDto.Simple>> getReplyReceivedPost() {
        return ResponseEntity.ok(postService.replyReceivedPost());
    }

    @ApiOperation("답장편지 - 보낸편지")
    @GetMapping("/reply/sent")
    public ResponseEntity<List<PostResponseDto.Simple>> getReplySentPost() {
        return ResponseEntity.ok(postService.replySentPost());
    }

    /*@ApiOperation("음식 검색. 제목과 내용, 카테고리를 이용 -es search")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<PostDocument>> searchFood(@ModelAttribute PostRequestDto.Search search, @PageableDefault(size = 8) Pageable pageable) {
        return ResponseEntity.ok(postService.searchPost(search, pageable));
    }*/
}
