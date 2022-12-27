package paperplane.paperplane.domain.Interest.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.Interest.repository.InterestRepository;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.repository.GroupRepository;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class InterestService {
    private final InterestRepository interestRepository;


    public Interest getInterestByKeyword(String keyword){
        return interestRepository.findByKeyword(keyword).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 키워드를 찾을 수 없습니다."));
    }
}
