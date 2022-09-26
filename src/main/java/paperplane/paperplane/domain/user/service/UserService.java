package paperplane.paperplane.domain.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import paperplane.paperplane.domain.post.repository.PostRepository;
import paperplane.paperplane.domain.user.repository.UserRepository;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
}
