package paperplane.paperplane.domain.post.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import paperplane.paperplane.domain.Interest.repository.InterestRepository;
import paperplane.paperplane.domain.post.repository.PostRepository;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
}
