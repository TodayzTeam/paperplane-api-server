package paperplane.paperplane.domain.group.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.dto.GroupRequestDto;
import paperplane.paperplane.domain.group.repository.GroupRepository;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.repository.UserRepository;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.usergroup.repository.UserGroupRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;


@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    public Group getGroupByCode(String code){
        return groupRepository.findByCode(code).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 그룹을 찾을 수 없습니다."));
    }

    public Group getGroupById(Integer id){
        return groupRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 그룹을 찾을 수 없습니다."));
    }

    public List<Group> getMyGroupList()throws Exception{
        Integer userId=getLoginUser();
        if(getLoginUser()==null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"다시 로그인하세요");
        }
        return userGroupRepository.getMyGroupList(userId);
    }

    public Integer getLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal().equals("anonymousUser")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"다시 로그인하세요");
        }
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

    public Integer createGroup(GroupRequestDto.Create create){
        checkDuplicateGroup(create.getName());

        Integer userId = getLoginUser();
        User user = userRepository.findById(userId).get();
        Group group = create.toEntity();
        groupRepository.save(group);

        UserGroup userGroup = UserGroup.builder()
                        .user(user)
                        .group(group)
                        .joinDate(LocalDateTime.now())
                        .build();

        userGroupRepository.save(userGroup);

        return group.getId();
    }

    public Group joinGroup(GroupRequestDto.GroupCode groupCode){
        Integer userId = getLoginUser();
        User user = userRepository.findById(userId).get();
        Group group = getGroupByCode(groupCode.getCode());

        if(userGroupRepository.findByCodeAndUserId(group.getId(), userId).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입한 그룹입니다.");
        }

        UserGroup userGroup = UserGroup.builder()
                .group(group)
                .user(user)
                .joinDate(LocalDateTime.now())
                .build();

        userGroupRepository.save(userGroup);

        return group;
    }

    public void resignGroup(GroupRequestDto.GroupCode groupCode){
        Group group = getGroupByCode(groupCode.getCode());
        Integer userId = getLoginUser();

        //그룹에 가입했는지 확인
        UserGroup userGroup = userGroupRepository.findByCodeAndUserId(group.getId(), userId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "가입한 그룹이 아닙니다."));

        List<UserGroup> userGroups = userGroupRepository.findTop2ByGroup_Id(group.getId());

        //마지막 사람이 탈퇴 시 자동으로 그룹 삭제됨
        if(userGroups.size() == 1){
            groupRepository.delete(group);
        }

        userGroupRepository.delete(userGroup);
    }

    public List<User> getGroupMemberListByGroupId(Integer groupId){
        return groupRepository.getGroupMemberListByGroupId(groupId);
    }

    public void checkDuplicateGroup(String name){
        if(groupRepository.existsByName(name)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 그룹이름입니다.");
        }
    }

    public List<User> getGroupUserByCode(String code)throws Exception{
        Group group = getGroupByCode(code);
        Integer userId = getLoginUser();

        List<User> userList=groupRepository.findGroupUser(userId,group.getId());
        if (userList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 코드에 대한 그룹이 존재하지 않습니다");
        }
        return userList;
    }
}
