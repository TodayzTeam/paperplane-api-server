package paperplane.paperplane.domain.post.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.Interest.repository.InterestRepository;
import paperplane.paperplane.domain.Interest.service.InterestService;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.repository.GroupRepository;
import paperplane.paperplane.domain.group.service.GroupService;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.post.PostColor;
import paperplane.paperplane.domain.post.dto.PostRequestDto;
import paperplane.paperplane.domain.post.dto.PostResponseDto;
import paperplane.paperplane.domain.post.repository.PostRepository;
import paperplane.paperplane.domain.postinterest.PostInterest;
import paperplane.paperplane.domain.postinterest.service.PostInterestService;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.repository.UserRepository;
import paperplane.paperplane.domain.user.service.UserService;
import paperplane.paperplane.domain.userpost.UserPost;
import paperplane.paperplane.domain.userpost.service.UserPostService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final InterestService interestService;
    private final GroupService groupService;
    private final PostInterestService postInterestService;
    private final UserService userService;
    private final UserPostService userPostService;
    private final GroupRepository groupRepository;
    public Integer addPost(PostRequestDto.Create data) throws Exception{
        Post post= new Post();
        if(groupRepository.findByCode(data.getCode()).isPresent()) {
            post= Post.builder()
                    .group(groupService.getGroupByCode(data.getCode()))
                    .title(data.getTitle())
                    .content(data.getContent())
                    .date(LocalDateTime.now())
                    .likeCount(0)
                    .reportCount(0)
                    .postColor(PostColor.valueOf(data.getColor()))
                    .sender(userService.getCurrentUser())
                    .build();
        }
        else{
            //save post
            post= Post.builder()
                    .title(data.getTitle())
                    .content(data.getContent())
                    .date(LocalDateTime.now())
                    .likeCount(0)
                    .reportCount(0)
                    .postColor(PostColor.valueOf(data.getColor()))
                    .sender(userService.getCurrentUser())
                    .build();
        }
        postRepository.save(post);




        //save postInterest
        JSONParser parser=new JSONParser();
        JSONArray keywordArray= (JSONArray) parser.parse(data.getKeyword()); //keyword parsing objcet

        Set<PostInterest> postInterestSet= new HashSet<>();
        log.info("{}",keywordArray.size());
        for(int i=0;i<keywordArray.size();i++){
            if(keywordArray.get(i)!=null) {
                String keyword = keywordArray.get(i).toString();
                Interest interest=interestService.addInterest(keyword);
                PostInterest postInterest =PostInterest.builder()
                        .post(post)
                        .interest(interest)
                        .build();
                postInterestService.addPostInterest(postInterest);
                postInterestSet.add(postInterest);
            }
        }


        //user api 추가 후 변경예정
        List<User> randUser=userService.getRandUser(data.getReceiveGroup());

        for(User user: randUser){
            userPostService.addUserPost(UserPost.builder()
                    .post(post)
                    .isReply(false)
                    .isRead(false)
                    .isReport(false)
                    .isLike(false)
                    .receiver(user)
                    .build());
        }
        //save userPost
        return 1;
    }
    public void removePost(Integer id){
         postRepository.delete(getByPostId(id));
    }
    public Post getByPostId(Integer id){
        return postRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 편지를 찾을 수 없습니다."));
    }

    public List<PostResponseDto.Simple> SearchPostByWord(String word, Pageable pageable){
        Page<Post> postPage =postRepository.findAllByWord(word,pageable);
        List<Post> post=postPage.stream().collect(Collectors.toList());
        return PostResponseDto.Simple.of(post);
    }
    public List<PostResponseDto.Simple> getPopularPost(){
        return PostResponseDto.Simple.of(postRepository.findTop8ByOrderByLikeCountDesc());
    }
    public List<PostResponseDto.Simple> getSentPost(Pageable pageable){
        User user=userService.getCurrentUser();
        Page<Post> postPage =postRepository.findSentPost(user.getId(),pageable);
        List<Post> post=postPage.stream().collect(Collectors.toList());
        return PostResponseDto.Simple.of(post);
    }
    public List<PostResponseDto.Simple> getReceivedPost(Pageable pageable){
        User user= userService.getCurrentUser();
        Page<Post> postPage= postRepository.findReceivedPost(user.getId(),pageable);
        List<Post> post=postPage.stream().collect(Collectors.toList());
        return PostResponseDto.Simple.of(post);
    }

    @Cacheable(value = "post-likecount", key = "#id", cacheManager = "cacheManager")
    public Integer getLikeCountByPostId(Integer id){
        return getByPostId(id).getLikeCount();
    }

    @CachePut(value = "post-likecount", key = "#id", cacheManager = "cacheManager")
    public Integer increasePostLikeCount (Integer id) throws Exception{
        Post post=getByPostId(id);
        User user=userService.getCurrentUser();
        log.info("{}",user.getId());
        log.info("{}",post.getId());
        UserPost userPost=userPostService.getByReceiverIdAndPostId(user.getId(),post.getId());
        log.info("2");
        userPostService.checkingLike(userPost);
        post.setLikeCount(post.getLikeCount()+1);
        postRepository.save(post);
        return post.getLikeCount();
    }
    public Integer increasePostReportCount(Integer id){
        Post post=getByPostId(id);
        post.setLikeCount(post.getReportCount()+1);
        postRepository.save(post);
        return post.getLikeCount();
    }

    public List<PostResponseDto.Simple> getLikedPost(Pageable pageable){
        User user= userService.getCurrentUser();
        Page<Post> postPage= postRepository.findLikedPost(pageable);
        List<Post> post=postPage.stream().collect(Collectors.toList());
        return PostResponseDto.Simple.of(post);
    }


}
