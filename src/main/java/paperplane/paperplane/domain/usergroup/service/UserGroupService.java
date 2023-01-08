package paperplane.paperplane.domain.usergroup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.usergroup.repository.UserGroupRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserGroupService {
    private final UserGroupRepository userGroupRepository;

    public Integer saveUserGroup(UserGroup userGroup){
        return userGroupRepository.save(userGroup).getId();
    }

    public List<UserGroup> getUserGroupByUserId(Integer userId){
        return userGroupRepository.findByUserId(userId);
    }

    public List<UserGroup> getUserGroupByGroupName(String name){
        return userGroupRepository.findByGroupName(name);
    }


}
