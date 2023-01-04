package paperplane.paperplane.domain.Interest.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.Interest.dto.InterestRequestDto;
import paperplane.paperplane.domain.Interest.dto.InterestResponseDto;
import paperplane.paperplane.domain.Interest.repository.InterestRepository;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.repository.GroupRepository;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.service.UserService;
import paperplane.paperplane.domain.userinterest.service.UserInterestService;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class InterestService {
    private final InterestRepository interestRepository;
    private final UserService userService;
    private final UserInterestService userInterestService;


    public Interest getInterestByKeyword(String keyword){
        return interestRepository.findByKeyword(keyword).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 키워드를 찾을 수 없습니다."));
    }
    public Interest addInterest(String keyword){
        if (interestRepository.findByKeyword(keyword).isPresent()) {
            Interest interest = interestRepository.findByKeyword(keyword).get();
            interest.setCount(interest.getCount() + 1);
            interestRepository.save(interest);
            return interest;
        } else {
            Interest interest=interestRepository.save(Interest.builder()
                    .count(1)
                    .keyword(keyword)
                    .build());
            return interest;
        }
    }

    public List<InterestResponseDto.Info> searchInterestByKeyword(String interest){
        return InterestResponseDto.Info.of( interestRepository.findAllByKeyword(interest));
    }

    public List<InterestResponseDto.Info> getMyInterest(){
        User user=userService.getCurrentUser();
        return InterestResponseDto.Info.of(interestRepository.findMyInterest(user.getId()));
    }
    public List<InterestResponseDto.Info> recommendInterest(){
        return InterestResponseDto.Info.of(interestRepository.findTop8ByOrderByCountDesc());
    }

}
