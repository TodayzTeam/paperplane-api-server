package paperplane.paperplane.domain.userinterest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
}
