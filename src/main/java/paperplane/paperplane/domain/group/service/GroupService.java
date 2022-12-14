package paperplane.paperplane.domain.group.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.dto.GroupRequestDto;
import paperplane.paperplane.domain.group.repository.GroupRepository;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.dto.UserResponseDto;
import paperplane.paperplane.domain.user.repository.UserRepository;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.usergroup.UserRole;
import paperplane.paperplane.domain.usergroup.repository.UserGroupRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


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
    
    public List<Group> getGroupListByName(String name){
        return groupRepository.findByNameContaining(name);
    }

    public List<Group> getMyGroupList(Integer userId){
        return userGroupRepository.getMyGroupList(userId);
    }

    public Integer createGroup(GroupRequestDto.Create create, Integer userId){
        checkDuplicateGroup(create.getName());

        User user = userRepository.findById(userId).get();
        Group group = create.toEntity();
        groupRepository.save(group);

        UserGroup userGroup = UserGroup.builder()
                        .user(user)
                        .group(group)
                        .joinDate(LocalDateTime.now())
                        .userRole(UserRole.OWNER)
                        .build();

        userGroupRepository.save(userGroup);

        return group.getId();
    }

    public void deleteGroup(GroupRequestDto.GroupCode groupCode, String email){
        Group group=getGroupByCode(groupCode.getCode());
        groupRepository.delete(group);
    }

    public Group joinGroup(GroupRequestDto.GroupCode groupCode, Integer userId){
        User user = userRepository.findById(userId).get();
        Group group = getGroupByCode(groupCode.getCode());

        if(userGroupRepository.findByCodeAndEmail(group.getId(), userId).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입한 그룹입니다.");
        }

        UserGroup userGroup = UserGroup.builder()
                .group(group)
                .user(user)
                .joinDate(LocalDateTime.now())
                .userRole(UserRole.READER)
                .build();

        userGroupRepository.save(userGroup);

        return group;
    }

    public void resignGroup(GroupRequestDto.GroupCode groupCode, Integer userId){
        Group group = getGroupByCode(groupCode.getCode());
        //그룹에 가입했는지 & 그룹장은 탈퇴 못함
        Optional<UserGroup> userGroupOptional = userGroupRepository.findByCodeAndEmail(group.getId(), userId);
        if(userGroupOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가입한 그룹이 아닙니다.");
        }
        UserGroup userGroup = userGroupOptional.get();
        if(userGroup.getUserRole().equals(UserRole.OWNER)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "그룹장은 탈퇴할 수 없습니다.");
        }
        userGroupRepository.delete(userGroup);
    }

    public PageImpl<UserResponseDto.Simple> getGroupMemberListByName(String name, Pageable pageable){
        Page<User> page = groupRepository.getGroupMemberListByName(name, pageable);
        List<User> groupMemberList = page.stream().collect(Collectors.toList());
        return new PageImpl<>(UserResponseDto.Simple.of(groupMemberList), pageable, page.getTotalPages());
    }

    public void checkDuplicateGroup(String name){
        if(groupRepository.existsByName(name)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 그룹이름입니다.");
        }
    }

    public List<User> getGroupUserByCode(String code)throws Exception{
        List<User> userList=groupRepository.findGroupUserByCode(code);
        if (userList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 코드에 대한 그룹이 존재하지 않습니다");
        }
        return userList;
    }
}
