package paperplane.paperplane.domain.post.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.Interest.repository.InterestRepository;
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
import paperplane.paperplane.domain.user.service.UserService;
import paperplane.paperplane.domain.userpost.UserPost;
import paperplane.paperplane.domain.userpost.service.UserPostService;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final InterestRepository interestRepository;
    private final GroupService groupService;
    private final PostInterestService postInterestService;
    private final UserService userService;
    private final UserPostService userPostService;
    public Integer newPost(PostRequestDto.Create data) throws Exception{
        //save post
        Post post= Post.builder()
                .group(groupService.getGroupByCode(data.getCode()))
                .title(data.getTitle())
                .content(data.getContent())
                .date(LocalDateTime.now())
                .likeCount(0)
                .reportCount(0)
                .postColor(PostColor.valueOf(data.getColor()))
                .sender(userService.getCurrentUser())
                .build();
        postRepository.save(post);

        //save postInterest
        JSONParser parser=new JSONParser();
        JSONArray keywordArray= (JSONArray) parser.parse(data.getKeyword()); //keyword parsing objcet

        Set<PostInterest> postInterestSet= new HashSet<>();
        for(int i=0;i<keywordArray.size();i++){
            String keyword= keywordArray.get(i).toString();
            Interest interest= new Interest();

            if(interestRepository.findByKeyword(keyword.toString()).isPresent()){
                interest=interestRepository.findByKeyword(keyword.toString()).get();
                interest.setCount(interest.getCount()+1);
                interestRepository.save(interest);
            }
            else{
                interestRepository.save(Interest.builder()
                        .count(1)
                        .keyword(keyword.toString())
                        .build());
            }

            PostInterest postInterest =PostInterest.builder()
                    .interest(interest)
                    .build();
            postInterestService.addPostInterest(postInterest);
            postInterestSet.add(postInterest);
        }


        //save userPost
        userPostService.addUserPost(UserPost.builder()
                .isReply(false)
                .isRead(false)
                .isReport(false)
                .receiver(userService.getUserByEmail(data.getReceiver()))
                .build());

        return 1;
    }
    public void removePost(Integer id){
         postRepository.delete(getByPostId(id));
    }
    public Post getByPostId(Integer id){
        return postRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 편지를 찾을 수 없습니다."));
    }


}
