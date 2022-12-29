package paperplane.paperplane.domain.userpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import paperplane.paperplane.domain.userpost.UserPost;
import paperplane.paperplane.domain.userpost.repository.UserPostRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPostService {
    private final UserPostRepository userPostRepository;

    public Integer addUserPost(UserPost userPost){
        return userPostRepository.save(userPost).getId();
    }
}
