package paperplane.paperplane.domain.postinterest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import paperplane.paperplane.domain.postinterest.PostInterest;
import paperplane.paperplane.domain.postinterest.repository.PostInterestRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostInterestService {
    private final PostInterestRepository postInterestRepository;

    public Integer addPostInterest(PostInterest postInterest){
        return postInterestRepository.save(postInterest).getId();
    }
}
