package paperplane.paperplane.domain.userinterest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import paperplane.paperplane.domain.userinterest.repository.UserInterestRepository;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserInterestService {
    private final UserInterestRepository userInterestRepository;


}
