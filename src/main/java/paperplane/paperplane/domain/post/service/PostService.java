package paperplane.paperplane.domain.post.service;


import com.nimbusds.jose.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.Interest.Interest;
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
import paperplane.paperplane.domain.user.service.UserService;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.usergroup.repository.UserGroupRepository;
import paperplane.paperplane.domain.userpost.UserPost;
import paperplane.paperplane.domain.userpost.repository.UserPostRepository;
import paperplane.paperplane.domain.userpost.service.UserPostService;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
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
    private final UserPostRepository userPostRepository;
    private final UserGroupRepository userGroupRepository;
    //private final ElasticsearchOperations elasticsearchOperations;

    //private final PostDocumentRepository postDocumentRepository;
    public Integer addPost(PostRequestDto.Create data) throws Exception{
        Post post= new Post();
        User user= userService.getUserById(userService.getLoginUser());
        if(data.getIsReply()){
            getByPostId(data.getOriginId());
        }

        if(groupRepository.findByCode(data.getGroupCode()).isPresent()) {
            post= Post.builder()
                    .group(groupService.getGroupByCode(data.getGroupCode()))
                    .title(data.getTitle())
                    .content(data.getContent())
                    .date(LocalDateTime.now())
                    .likeCount(0)
                    .reportCount(0)
                    .postColor(PostColor.valueOf(data.getColor()))
                    .sender(user)
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
                    .sender(user)
                    .build();
        }
        postRepository.save(post);


        //user api 추가 후 변경예정
        List<User> randUser= new ArrayList<>();
        //user api 추가 후 변경예정
        if(Objects.equals(data.getReceiveGroup(), "RAND")) {
            randUser = userService.getRandUser(data.getReceiveGroup());
        } else if (Objects.equals(data.getReceiveGroup(),"GROUP")) {
            if(data.getGroupCode()==null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"group code를 넣어주세요");
            }
            randUser = userService.getRandUser(data.getGroupCode());
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 receiveGROUP 입니다.");

        while(randUser.isEmpty()){
            randUser=userService.getRandUser(data.getReceiveGroup());
        }
        for(User receive: randUser){
            if(data.getIsReply()) {
                if(data.getOriginId()==null){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"originId -원본편지의 id를 추가 필요");
                }
                if(!userPostService.checkReply(data.getOriginId())){
                    postRepository.delete(post);
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이미 회신한 편지입니다.");
                }

                userPostService.addUserPost(UserPost.builder()
                        .post(post)
                        .sentReply(false)
                        .receivedReply(true)
                        .isRead(false)
                        .isReport(false)
                        .isLike(false)
                        .receiver(getByPostId(data.getOriginId()).getSender())
                        .replyId(data.getOriginId())
                        .build());
                UserPost userPostByReceiver=userPostService.getByReceiverIdAndPostId(post.getSender().getId(),data.getOriginId());
                userPostByReceiver.setReplyId(post.getId());
                userPostByReceiver.setReceivedReply(false);
                userPostByReceiver.setSentReply(false);
                userPostRepository.save(userPostByReceiver);

                UserPost userPostBySender=userPostService.getByReceiverIdAndPostId(receive.getId(),data.getOriginId());
                userPostBySender.setReplyId(post.getId());
                userPostBySender.setReceivedReply(false);
                userPostBySender.setSentReply(false);
                userPostRepository.save(userPostBySender);

                break;

            }else{
                userPostService.addUserPost(UserPost.builder()
                        .post(post)
                        .sentReply(false)
                        .receivedReply(false)
                        .isRead(false)
                        .isReport(false)
                        .isLike(false)
                        .receiver(receive)
                        .build());
            }
        }
        if(userPostService.getByReceiverIdAndPostId(user.getId(),post.getId()).getId()==null){
            if(data.getIsReply()) {
                UserPost userPost=UserPost.builder()
                        .post(post)
                        .sentReply(true)
                        .receivedReply(false)
                        .isRead(false)
                        .isReport(false)
                        .isLike(false)
                        .replyId(data.getOriginId())
                        .receiver(user)
                        .build();
                userPostService.addUserPost(userPost);
            }
            else {
                UserPost userPost=UserPost.builder()
                        .post(post)
                        .sentReply(false)
                        .receivedReply(false)
                        .isRead(false)
                        .isReport(false)
                        .isLike(false)
                        .receiver(user)
                        .build();
                userPostService.addUserPost(userPost);
            }
        }


        //save Interest
        if(data.getKeyword()!=null) {
            JSONParser parser = new JSONParser();
            JSONArray keywordArray = (JSONArray) parser.parse(data.getKeyword()); //keyword parsing objcet

            Set<PostInterest> postInterestSet = new HashSet<>();
            for (int i = 0; i < keywordArray.size(); i++) {
                if (keywordArray.get(i) != null) {
                    String keyword = keywordArray.get(i).toString();
                    Interest interest = interestService.addInterest(keyword);
                    PostInterest postInterest = PostInterest.builder()
                            .post(post)
                            .interest(interest)
                            .build();
                    postInterestService.addPostInterest(postInterest);
                    postInterestSet.add(postInterest);
                }
            }
        }


        //temp post 처리
        user.setTempPost(0);
        userService.saveUser(user);
        postRepository.save(post);

        //es search
        //postDocumentRepository.save(new PostDocument(post));
        return post.getId();
    }

    public Integer interStorePost(PostRequestDto.Create data)throws Exception{
        Post post= new Post();
        User user= userService.getUserById(userService.getLoginUser());
        if(data.getIsReply()){
            getByPostId(data.getOriginId());
        }

        if(groupRepository.findByCode(data.getGroupCode()).isPresent()) {
            post= Post.builder()
                    .group(groupService.getGroupByCode(data.getGroupCode()))
                    .title(data.getTitle())
                    .content(data.getContent())
                    .date(LocalDateTime.now())
                    .likeCount(0)
                    .reportCount(0)
                    .postColor(PostColor.valueOf(data.getColor()))
                    .sender(user)
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
                    .sender(user)
                    .build();
        }
        postRepository.save(post);


        //user api 추가 후 변경예정
        List<User> randUser= new ArrayList<>();
        //user api 추가 후 변경예정
        if(Objects.equals(data.getReceiveGroup(), "RAND")) {
            randUser = userService.getRandUser(data.getReceiveGroup());
        } else if (Objects.equals(data.getReceiveGroup(),"GROUP")) {
            if(data.getGroupCode()==null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"group code를 넣어주세요");
            }
            randUser = userService.getRandUser(data.getGroupCode());
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 receiveGROUP 입니다.");

        while(randUser.isEmpty()){
            randUser=userService.getRandUser(data.getReceiveGroup());
        }
        for(User receive: randUser){
            if(data.getIsReply()) {
                if(data.getOriginId()==null){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"originId -원본편지의 id를 추가 필요");
                }
                if(!userPostService.checkReply(data.getOriginId())){
                    postRepository.delete(post);
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이미 회신한 편지입니다.");
                }

                userPostService.addUserPost(UserPost.builder()
                        .post(post)
                        .sentReply(true)
                        .receivedReply(false)
                        .isRead(false)
                        .isReport(false)
                        .isLike(false)
                        .receiver(getByPostId(data.getOriginId()).getSender())
                        .replyId(data.getOriginId())
                        .build());
                UserPost userPost=userPostService.getByReceiverIdAndPostId(post.getSender().getId(),data.getOriginId());
                userPost.setReplyId(post.getId());
                userPost.setReceivedReply(true);
                userPost.setSentReply(false);
                userPostRepository.save(userPost);

                break;

            }else{
                userPostService.addUserPost(UserPost.builder()
                        .post(post)
                        .sentReply(false)
                        .receivedReply(false)
                        .isRead(false)
                        .isReport(false)
                        .isLike(false)
                        .receiver(receive)
                        .build());
            }
        }
        if(userPostService.getByReceiverIdAndPostId(user.getId(),post.getId()).getId()==null){
            UserPost userPost=UserPost.builder()
                    .post(post)
                    .sentReply(false)
                    .receivedReply(false)
                    .isRead(false)
                    .isReport(false)
                    .isLike(false)
                    .receiver(user)
                    .build();
            userPostService.addUserPost(userPost);

        }




        //save Interest
        if(data.getKeyword()!=null) {
            JSONParser parser = new JSONParser();
            JSONArray keywordArray = (JSONArray) parser.parse(data.getKeyword()); //keyword parsing objcet

            Set<PostInterest> postInterestSet = new HashSet<>();
            for (int i = 0; i < keywordArray.size(); i++) {
                if (keywordArray.get(i) != null) {
                    String keyword = keywordArray.get(i).toString();
                    Interest interest = interestService.addInterest(keyword);
                    PostInterest postInterest = PostInterest.builder()
                            .post(post)
                            .interest(interest)
                            .build();
                    postInterestService.addPostInterest(postInterest);
                    postInterestSet.add(postInterest);
                }
            }
        }

        //temp post 처리
        user.setTempPost(post.getId());
        userService.saveUser(user);
        postRepository.save(post);

        return post.getId();
    }
    public List<PostResponseDto.Info> PostInfoById(Integer postId){
        Post post=getByPostId(postId);

        User user= userService.getUserById(userService.getLoginUser());
        log.info("{}",user.getId());
        log.info("{}",post.getSender().getId());

        Optional<UserPost> userPostByReceiver=userPostRepository.findByReceiverIdAndPostId(user.getId(),postId);
        Optional<UserPost> userPostBySender=userPostRepository.findByReceiverIdAndPostId(post.getSender().getId(),postId);

        log.info("{}",userPostBySender.get().getId());
        log.info("{}",userPostByReceiver.get().getId());
        if(userPostBySender.isPresent()){
            if(userPostBySender.get().getSentReply()||userPostBySender.get().getReceivedReply()){
                userPostByReceiver=userPostBySender;
            }
        }

        List<PostResponseDto.Info> infos= new ArrayList<>();
        if(post.getGroup()==null){
            infos.add(PostResponseDto.Info.of(Post.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .postInterests(post.getPostInterests())
                    .postColor(post.getPostColor())
                    .likeCount(post.getLikeCount())
                    .sender(post.getSender())
                    .reportCount(post.getReportCount())
                    .date(post.getDate())
                    .content(post.getContent())
                    .userPosts(post.getUserPosts())
                    .group(new Group())
                    .build()));
        }else {
            infos.add(PostResponseDto.Info.of(post));
        }
            UserPost up=userPostByReceiver.get();
            if(up.getReplyId()!=null) {
                post=getByPostId(userPostByReceiver.get().getReplyId());
                if(post.getGroup()==null){
                    infos.add(PostResponseDto.Info.of(Post.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .postInterests(post.getPostInterests())
                            .postColor(post.getPostColor())
                            .likeCount(post.getLikeCount())
                            .sender(post.getSender())
                            .reportCount(post.getReportCount())
                            .date(post.getDate())
                            .content(post.getContent())
                            .userPosts(post.getUserPosts())
                            .group(new Group())
                            .build()));
                }else {
                    infos.add(PostResponseDto.Info.of(post));
                }
            }
            up.setIsRead(true);
            userPostRepository.save(up);


        return infos;
    }
    public Integer checkingTempPost(){
        User user= userService.getUserById(userService.getLoginUser());
        return user.getTempPost();
    }
    public void removePost(Integer id){
        postRepository.delete(getByPostId(id));
        //postDocumentRepository.delete(postDocumentRepository.findById(id).get());
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
        User user= userService.getUserById(userService.getLoginUser());
        Page<Post> postPage =postRepository.findSentPost(user.getId(),pageable);
        List<Post> post=postPage.stream().collect(Collectors.toList());
        return PostResponseDto.Simple.of(post);
    }
    public List<PostResponseDto.Simple> getReceivedPost(Pageable pageable){
        User user= userService.getUserById(userService.getLoginUser());
        Page<Post> postPage= postRepository.findReceivedPost(user.getId(),pageable);
        List<Post> post=postPage.stream().collect(Collectors.toList());
        return PostResponseDto.Simple.of(post);
    }

    @Cacheable(value = "post-likecount", key = "#id", cacheManager = "cacheManager")
    public Integer getLikeCountByPostId(Integer id){
        return getByPostId(id).getLikeCount();
    }

    public Integer increasePostLikeCount (Integer id) throws Exception{
        Post post=getByPostId(id);
        User user= userService.getUserById(userService.getLoginUser());
        UserPost userPost=userPostService.getByReceiverIdAndPostId(user.getId(),post.getId());
        if(userPost.getId()!=null){
            userPostService.checkLike(userPost);
            post.setLikeCount(post.getLikeCount()+1);
            postRepository.save(post);
            return post.getLikeCount();
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"해당 편지를 소유하고 있지 않습니다");

    }

    public Integer decreasePostLikeCount (Integer id) throws Exception{
        Post post = getByPostId(id);
        User user = userService.getUserById(userService.getLoginUser());
        UserPost userPost=userPostService.getByReceiverIdAndPostId(user.getId(),post.getId());
        if(userPost.getId()!=null){
            userPostService.cancelLike(userPost);
            post.setLikeCount(post.getLikeCount()-1);
            postRepository.save(post);
            return post.getLikeCount();
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"해당 편지를 소유하고 있지 않습니다");

    }

    public Integer increasePostReportCount(Integer id)throws Exception{
        Post post=getByPostId(id);
        User user= userService.getUserById(userService.getLoginUser());
        UserPost userPost=userPostService.getByReceiverIdAndPostId(user.getId(),post.getId());
        userPostService.checkingReport(userPost);
        post.setReportCount(post.getReportCount()+1);
        if(post.getReportCount()>=5){
            removePost(post.getId());
            throw new ResponseStatusException(HttpStatus.OK,"5회 신고 누적으로 편지 삭제");
        }
        else {
            postRepository.save(post);
        }
        return post.getLikeCount();
    }

    public List<PostResponseDto.Simple> getLikedPost(Pageable pageable){
        User user= userService.getUserById(userService.getLoginUser());
        if(postRepository.findLikedPost(user.getId(),pageable).isEmpty()){
            List<PostResponseDto.Simple> simpleList=new ArrayList<>();
            return simpleList;
        }
        else {
            return PostResponseDto.Simple.of(postRepository.findLikedPost(user.getId(),pageable).stream().collect(Collectors.toList()));
        }
    }

    public List<PostResponseDto.Simple> searchGroupPostByWord(Integer groupId,String word,Pageable pageable){
        User user= userService.getUserById(userService.getLoginUser());
        Page<Post> postPage =postRepository.findGroupPostByWord(groupId,word,pageable);
        List<Post> post=postPage.stream().collect(Collectors.toList());
        return PostResponseDto.Simple.of(post);
    }

    public List<PostResponseDto.Simple> getGroupPost(Integer groupId){
        //가입했는지 확인
        User user = userService.getUserById(userService.getLoginUser());
        Optional<UserGroup> userGroupOptional = userGroupRepository.findByCodeAndUserId(groupId, user.getId());

        if(userGroupOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가입하지 않은 그룹입니다.");
        }

        //가입 이전에 써진 편지는 볼 수 없음
        UserGroup userGroup = userGroupOptional.get();
        LocalDateTime joinDate = userGroup.getJoinDate();

        List<Post> groupPost = postRepository.findGroupPost(groupId, joinDate);

        return PostResponseDto.Simple.of(groupPost);
    }

    /*public Page<PostDocument> searchPost(PostRequestDto.Search search,Pageable pageable){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        if (search.getQuery() != null) {
            MultiMatchQueryBuilder matchQuery = QueryBuilders.multiMatchQuery(search.getQuery(), "content", "title");
            queryBuilder = queryBuilder.withQuery(matchQuery);
        }

        NativeSearchQuery searchQuery = queryBuilder.withPageable(pageable).build();
        SearchHits<PostDocument> searchHits = elasticsearchOperations.search(searchQuery, PostDocument.class);
        SearchPage<PostDocument> searchPage = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());
        return (Page<PostDocument>) SearchHitSupport.unwrapSearchHits(searchPage);
    }*/

}
