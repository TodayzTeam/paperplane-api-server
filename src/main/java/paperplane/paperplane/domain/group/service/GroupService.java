package paperplane.paperplane.domain.group.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.repository.GroupRepository;
import paperplane.paperplane.domain.user.User;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;

    public Group getGroupByCode(String code){
        return groupRepository.findByCode(code).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 그룹을 찾을 수 없습니다."));
    }
    public List<User> getGroupUserByCode(String code)throws Exception{
        List<User> userList=groupRepository.findGroupUserByCode(code);
        if (userList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 코드에 대한 그룹 유저가 존재하지 않습니다");
        }
        return userList;
    }
}
