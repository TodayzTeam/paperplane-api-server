package paperplane.paperplane.domain.userpost.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.post.repository.PostRepository;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.service.UserService;
import paperplane.paperplane.domain.userpost.UserPost;
import paperplane.paperplane.domain.userpost.dto.UserPostResponseDto;
import paperplane.paperplane.domain.userpost.repository.UserPostRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserPostService {
    private final UserPostRepository userPostRepository;
    private final UserService userService;
    private final PostRepository postRepository;

    public Integer addUserPost(UserPost userPost){
        return userPostRepository.save(userPost).getId();
    }

    public UserPost getByReceiverIdAndPostId(Integer userId,Integer postId){
        return userPostRepository.findByReceiverIdAndPostId(userId,postId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"편지 소유/존재 여부 확인"));
    }
    public void checkingLike(UserPost userPost)throws Exception{
        if(userPost.getIsLike()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이미 좋아요를 눌렀습니다.");
        }
        else {
            userPost.setIsLike(true);
            userPostRepository.save(userPost);
        }
    }
    public void checkingReport(UserPost userPost)throws Exception{
        if(userPost.getIsReport()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이미 신고를 했습니다.");
        }
        else {
            userPost.setIsReport(true);
            userPostRepository.save(userPost);
        }
    }
    public UserPostResponseDto getPostOption(Integer postId){
        User user= userService.getCurrentUser();/*userPostRepository*/
        UserPost userPost=userPostRepository.findPostOptionByPostId(user.getId(),postId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"편지 소유/존재 여부 확인"));
        log.info("{}",userPost.getId());

        return UserPostResponseDto.of(userPost);
    }
}
