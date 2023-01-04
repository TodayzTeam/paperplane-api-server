package paperplane.paperplane.domain.userpost.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.userpost.UserPost;
import paperplane.paperplane.domain.userpost.repository.UserPostRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserPostService {
    private final UserPostRepository userPostRepository;

    public Integer addUserPost(UserPost userPost){
        return userPostRepository.save(userPost).getId();
    }

    public UserPost getByReceiverIdAndPostId(Integer userId,Integer postId){
        log.info("{}",userPostRepository.findByReceiverIdAndPostId(userId,postId));
        return userPostRepository.findByReceiverIdAndPostId(userId,postId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 user,post id에 대한 User, Post를 못찾았습니다."));
    }
    public void checkingLike(UserPost userPost)throws Exception{
        if(userPost.getIsLike()){
            log.info("t");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이미 좋아요를 눌렀습니다.");
        }
        else {
            userPost.setIsLike(true);
            userPostRepository.save(userPost);
            log.info("f");
        }
    }
}
