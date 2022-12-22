package paperplane.paperplane.domain.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.post.repository.PostRepository;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final HttpServletRequest request;


    private Integer getUserIdInHeader() {
        String userIdString = request.getHeader("userId");
        log.info("id");
        log.info(userIdString);

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

    public Optional<User> getUserByEmail(String email) {return userRepository.findByEmail(email);}

    public User getUser(Integer id){
        return userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 회원이 없습니다."));
    }

    public void deleteUser(User user) {
        userRepository.delete(userRepository.findById(user.getId()).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 회원이 없습니다.")));
    }
}
