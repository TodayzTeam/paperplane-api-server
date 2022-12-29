package paperplane.paperplane.domain.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.Interest.Interest;

import paperplane.paperplane.domain.Interest.repository.InterestRepository;
import paperplane.paperplane.domain.Interest.service.InterestService;
import paperplane.paperplane.domain.post.repository.PostRepository;
import paperplane.paperplane.domain.postinterest.PostInterest;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.dto.UserRequestDto;
import paperplane.paperplane.domain.user.repository.UserRepository;
import paperplane.paperplane.domain.userinterest.UserInterest;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
  //  private final UserInterestService userInterestService;
  //  private final InterestService interestService;
    private final HttpServletRequest request;


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


    public User getCurrentUser() {
        return userRepository.findById(getUserIdInHeader()).get();
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 유저를 찾을 수 없습니다."));
    }
    public User getRandUser(String randUser) throws Exception{
        log.info("{}",randUser);

        if(randUser.equals("RAND")){
            return userRepository.findById(1).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 유저를 찾을 수 없습니다."));
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 수신자그룹입니다.");
        }

    }

    public User getUser(Integer id){
        return userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 회원이 없습니다."));
    }

   /* public User getUserInfo(Integer id){
        User user = getUser(id);

        List<UserInterest> userInterests = userInterestService.getUserInterestByUserId(id);


    }
*/
    public void deleteUser(User user) {
        userRepository.delete(userRepository.findById(user.getId()).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 회원이 없습니다.")));
    }

    /*public User updateUser(Integer id, UserRequestDto.Profile profile) throws ParseException {
        User user = getUser(id);
        user.update(profile);

        JSONParser parser = new JSONParser();
        JSONArray keywordArray = (JSONArray) parser.parse(profile.getInterest());

        for(int i=0;i<keywordArray.size();i++){
            String keyword = keywordArray.get(i).toString();

            Interest interest = interestService.getInterestByKeyword(keyword);

            UserInterest userInterest = UserInterest.builder()
                    .interest(interest)
                    .build();

            userInterestService.addUserInterest(userInterest);
            user.getUserInterests().add(userInterest);
        }

        return user;
    }*/

    public String updateProfileImage(Integer id, MultipartFile file) throws IOException {
        //String uploadPath = "/home/ubuntu/app/img/";
        String uploadPath = "C:\\study\\img\\";
        String uploadFileName = file.getOriginalFilename();

        uploadFileName = UUID.randomUUID().toString() + "_" + uploadFileName;

        File newFile = new File(uploadPath + uploadFileName);
        file.transferTo(newFile);

        User user = BY(id);

        user.setProfileImageUrl(newFile.toString());

        return user.getProfileImageUrl();
    }

}
