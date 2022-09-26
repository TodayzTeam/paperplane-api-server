package paperplane.paperplane.domain.Interest.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import paperplane.paperplane.domain.Interest.repository.InterestRepository;
import paperplane.paperplane.domain.group.repository.GroupRepository;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class InterestService {
    private final InterestRepository interestRepository;
}
