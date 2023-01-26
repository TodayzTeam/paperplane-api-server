package paperplane.paperplane.domain.userpost.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.service.UserService;
import paperplane.paperplane.domain.userpost.UserPost;
import paperplane.paperplane.domain.userpost.dto.UserPostRequestDto;
import paperplane.paperplane.domain.userpost.dto.UserPostResponseDto;
import paperplane.paperplane.domain.userpost.repository.UserPostRepository;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<UserPost> userPostOptional =userPostRepository.findByReceiverIdAndPostId(userId,postId);
        if(userPostOptional.isPresent()){
            return userPostRepository.findByReceiverIdAndPostId(userId,postId).get();
        }else {
            return new UserPost();
        }

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

    public void cancelLike(UserPost userPost){
        if(!userPost.getIsLike()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"좋아요를 누른 상태가 아닙니다.");
        }
        userPost.setIsLike(false);
        userPostRepository.save(userPost);
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
    public List<UserPostResponseDto.Option> getPostOption(UserPostRequestDto userPostRequestDto){
        List<Integer> postIdList= userPostRequestDto.getPostIdList();
        User user= userService.getUserById(userService.getLoginUser());

        List<UserPostResponseDto.Option> options=new ArrayList<>();
        for(Integer postId: postIdList){
            UserPost userPost = getByReceiverIdAndPostId(user.getId(), postId);
            if(userPost.getId()!=null){
                options.add(UserPostResponseDto.Option.of(userPost));
            }
        }

        return options;
    }

    public Boolean checkReply(Integer postId)throws Exception{
        User user= userService.getUserById(userService.getLoginUser());
        if (userPostRepository.countUserPostBySenderIdAndOriginId(user.getId(),postId)!=null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이미 회신을 했습니다.");
        }
        return true;
    }

}
