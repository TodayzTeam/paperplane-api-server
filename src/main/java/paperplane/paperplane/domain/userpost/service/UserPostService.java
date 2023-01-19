package paperplane.paperplane.domain.userpost.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.service.UserService;
import paperplane.paperplane.domain.userpost.UserPost;
import paperplane.paperplane.domain.userpost.dto.UserPostResponseDto;
import paperplane.paperplane.domain.userpost.repository.UserPostRepository;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserPostService {
    private final UserPostRepository userPostRepository;
    private final UserService userService;

    public Integer addUserPost(UserPost userPost){
        return userPostRepository.save(userPost).getId();
    }

    public UserPost getByReceiverIdAndPostId(Integer userId,Integer postId){
        return userPostRepository.findPostOptionByPostId(userId,postId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"편지 소유/존재 여부 확인"));
    }
    public void checkLike(UserPost userPost){
        if(userPost.getIsLike()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이미 좋아요를 눌렀습니다.");
        }
        else {
            userPost.setIsLike(true);
            userPostRepository.save(userPost);
        }
    }
    public void checkingReport(UserPost userPost){
        if(userPost.getIsReport()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이미 신고를 했습니다.");
        }
        else {
            userPost.setIsReport(true);
            userPostRepository.save(userPost);
        }
    }
    public UserPostResponseDto.Option getPostOption(Integer postId){
        User user= userService.getUserById(userService.getLoginUser());
        UserPost userPost=userPostRepository.findPostOptionByPostId(user.getId(),postId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"편지 소유/존재 여부 확인"));

        return UserPostResponseDto.Option.of(userPost);
    }

    public Boolean checkReply(Integer postId)throws Exception{
        User user= userService.getUserById(userService.getLoginUser());
        if (userPostRepository.countUserPostBySenderIdAndOriginId(user.getId(),postId)!=null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이미 회신을 했습니다.");
        }
        if(userPostRepository.findPostOptionByPostId(user.getId(),postId).isPresent()){
            UserPost userPost=userPostRepository.findPostOptionByPostId(user.getId(),postId).get();
            if (userPost.getIsReply()){
                return false;
            }
        }
        return true;
    }
}
