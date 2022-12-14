package paperplane.paperplane.domain.userinterest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import paperplane.paperplane.domain.userinterest.UserInterest;
import lombok.extern.slf4j.Slf4j;
import paperplane.paperplane.domain.userinterest.repository.UserInterestRepository;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserInterestService {
    private final UserInterestRepository userInterestRepository;

    public UserInterest addUserInterest(UserInterest userInterest){
        return userInterestRepository.save(userInterest);
    }
}
