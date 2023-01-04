package paperplane.paperplane.domain.group.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.dto.GroupRequestDto;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;
import paperplane.paperplane.domain.group.repository.GroupRepository;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.service.UserService;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.usergroup.UserRole;
import paperplane.paperplane.domain.usergroup.repository.UserGroupRepository;
import paperplane.paperplane.domain.usergroup.service.UserGroupService;

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
    private final UserService userService;
    private final UserGroupService userGroupService;
    private final UserGroupRepository userGroupRepository;

    public Group getGroupByCode(String code){
        return groupRepository.findByCode(code).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 그룹을 찾을 수 없습니다."));
    }

    public List<Group> getGroupListByName(String name){
        return groupRepository.findByNameContaining(name);
    }

    public List<Group> getMyGroupList(Integer userId){
        return userGroupService.getUserGroupByUserId(userId).stream().map(UserGroup::getGroup).collect(Collectors.toList());
    }

    public Integer createGroup(String email, GroupRequestDto.Create create){
        if(checkDuplicateGroup(create.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 그룹이름입니다.");
        }

        User user = userService.getUserByEmail(email);
        Group group = create.toEntity();
        groupRepository.save(group);

        UserGroup userGroup = UserGroup.builder()
                        .user(user)
                        .group(group)
                        .joinDate(LocalDateTime.now())
                        .userRole(UserRole.OWNER)
                        .build();

        group.setUserGroups(new HashSet<>(Arrays.asList(userGroup)));
        user.getUserGroups().add(userGroup);
        userGroupRepository.save(userGroup);

        return group.getId();
    }

    public void deleteGroup(GroupRequestDto.GroupCode groupCode, String email){

    }

    public Group joinGroup(GroupRequestDto.GroupCode groupCode, String email){
        if(userGroupRepository.findByGroupCodeAndUserEmail(groupCode.getCode(), email).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입한 그룹입니다.");
        }

        User user = userService.getUserWithUserGroupByEmail(email);
        Group group = getGroupByCode(groupCode.getCode());

        UserGroup userGroup = UserGroup.builder()
                .group(group)
                .user(user)
                .joinDate(LocalDateTime.now())
                .userRole(UserRole.READER)
                .build();

        userGroupRepository.save(userGroup);

        user.getUserGroups().add(userGroup);
        group.getUserGroups().add(userGroup);

        return group;
    }

    public ArrayList<UserGroup> getGroupMemberList(String name){
        return new ArrayList<>(userGroupService.getUserGroupByGroupName(name));
    }

    public boolean checkDuplicateGroup(String name){
        return groupRepository.existsByName(name);
    }

}
