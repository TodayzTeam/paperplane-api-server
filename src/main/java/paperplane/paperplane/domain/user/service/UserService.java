package paperplane.paperplane.domain.user.service;


import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.Interest.Interest;

import paperplane.paperplane.domain.Interest.repository.InterestRepository;
import paperplane.paperplane.domain.Interest.service.InterestService;
import paperplane.paperplane.domain.group.service.GroupService;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.post.repository.PostRepository;
import paperplane.paperplane.domain.postinterest.PostInterest;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.dto.UserRequestDto;
import paperplane.paperplane.domain.user.repository.UserRepository;
import paperplane.paperplane.domain.userinterest.UserInterest;
import paperplane.paperplane.domain.userinterest.service.UserInterestService;
import paperplane.paperplane.global.security.jwt.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final UserInterestService userInterestService;
    private final HttpServletRequest request;
    private final GroupService groupService;
    private final TokenService tokenService;

    private Integer getUserIdInHeader() {
        String userIdString = request.getHeader("userId");

        if (userIdString == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
            return Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID를 파싱할 수 없습니다.");
        }
    }

/*    public User getCurrentUser() {
        return userRepository.findById(getUserIdInHeader()).get();
    }*/

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 유저를 찾을 수 없습니다."));
    }

    public Integer getLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal().equals("anonymousUser")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"다시 로그인하세요");
        }
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

    public Integer getUserByToken(String token){
        if(token == null || !tokenService.verifyToken(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "access token이 유효하지 않습니다.");
        }
        String email = tokenService.getUid(token);
        return getUserByEmail(email).getId();
    }

    public List<User> getRandUser(String randUser) throws Exception{
        log.info("{}",randUser);

        //편지 제약조건에 따라 추후 수정
        if(randUser.equals("RAND")){
            List<User> users =userRepository.findRandUserList();
            while (users.isEmpty()||(users.size()==1 && users.get(0).getId().equals(getLoginUser()))){
                users =userRepository.findRandUserList();
            }
            return users;
        } else if (randUser.equals(groupService.getGroupByCode(randUser).getCode())) {
            return groupService.getGroupUserByCode(randUser);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 수신자그룹입니다.");
        }

    }

    public User getUserById(Integer id){
        return userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 회원이 없습니다."));
    }

    public void addInterest(String keyword) throws ParseException {
        Integer userId = getLoginUser();
        User user = getUserById(userId);

        JSONParser parser = new JSONParser();
        JSONArray keywordArray = (JSONArray) parser.parse(keyword);
        ArrayList array = (ArrayList) keywordArray.stream().distinct().collect(Collectors.toList());

        for(int i = 0; i < array.size(); i++){
            String kw = array.get(i).toString();
            if(kw.isEmpty()) continue;
            //키워드 없으면 추가
            Interest interest = interestRepository.findByKeyword(kw).orElseGet(()->
                    interestRepository.save(Interest.builder()
                            .keyword(kw)
                            .count(0)
                            .build()));

            UserInterest userInterest = UserInterest.builder()
                    .user(user)
                    .interest(interest)
                    .build();

            userInterestService.addUserInterest(userInterest);
        }
    }

    public void logout(){
        Integer userId = getLoginUser();
        User user = getUserById(userId);
        user.setRefreshToken(null);
    }

    public void deleteUser() {
        Integer userId = getLoginUser();
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    public User updateUser(UserRequestDto.Profile profile) throws ParseException {
        Integer userId = getLoginUser();
        User user = getUserById(userId);
        user.updateProfile(profile);

        JSONParser parser = new JSONParser();
        JSONArray keywordArray = (JSONArray) parser.parse(profile.getInterest());
        ArrayList array = (ArrayList) keywordArray.stream().distinct().collect(Collectors.toList());

        for(int i = 0; i < array.size(); i++){
            String keyword = array.get(i).toString();
            if(keyword.isEmpty()) continue;
            Interest interest = interestRepository.findByKeyword(keyword).orElseGet(()->
                    interestRepository.save(Interest.builder()
                            .keyword(keyword)
                            .count(0)
                            .build()));

            UserInterest userInterest = UserInterest.builder()
                    .user(user)
                    .interest(interest)
                    .build();

            userInterestService.addUserInterest(userInterest);
        }

        return user;
    }

    public String updateProfileImage(MultipartFile file) throws IOException {
        Integer userId = getLoginUser();
        String uploadPath = "/home/ubuntu/app/img/";
        String uploadFileName = file.getOriginalFilename();

        uploadFileName = UUID.randomUUID().toString() + "_" + uploadFileName;
        File newFile = new File(uploadPath + uploadFileName);
        file.transferTo(newFile);

        User user = getUserById(userId);
        user.setProfileImageUrl(newFile.toString());

        return user.getProfileImageUrl();
    }

    public List<Integer> getAllUserId(){
        List<User> users= userRepository.findAll();
        List<Integer> idList=new ArrayList<>();
        for(User user:users){
            idList.add(user.getId());
        }
        return idList;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }
}
