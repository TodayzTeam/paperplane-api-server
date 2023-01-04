package paperplane.paperplane.domain.group.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.dto.GroupRequestDto;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;
import paperplane.paperplane.domain.group.service.GroupService;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.usergroup.dto.UserGroupResponseDto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"Group Controller"})
@Slf4j
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @ApiOperation("그룹 생성")
    @PostMapping("/create")
    public ResponseEntity<Integer> createGroup(Authentication authentication, GroupRequestDto.Create create) throws Exception {
        String email = ((User)authentication.getPrincipal()).getEmail();

        return ResponseEntity.ok(groupService.createGroup(email, create));
    }

    @ApiOperation("그룹 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteGroup(@Valid GroupRequestDto.GroupCode groupCode, Authentication authentication) throws Exception {
        String email = ((User)authentication.getPrincipal()).getEmail();
        //  groupService.deleteGroup(groupCode, email);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("그룹 참가")
    @PostMapping("/join")
    public ResponseEntity<GroupResponseDto.Info> joinGroup(@Valid GroupRequestDto.GroupCode groupCode, Authentication authentication) throws Exception {
        String email = ((User)authentication.getPrincipal()).getEmail();

        return ResponseEntity.ok(GroupResponseDto.Info.of(groupService.joinGroup(groupCode, email)));
    }

    @ApiOperation("그룹 탈퇴")
    @PatchMapping("/resign")
    public ResponseEntity<Void> resignGroup(@Valid GroupRequestDto.GroupCode groupCode) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("그룹을 그룹명으로 검색")
    @GetMapping("search/{name}")
    public ResponseEntity<List<GroupResponseDto.Simple>> findGroup(@PathVariable String name) throws Exception {
        List<Group> groupList = groupService.getGroupListByName(name);
        return ResponseEntity.ok(GroupResponseDto.Simple.of(groupList));
    }

    @ApiOperation("해당 그룹의 그룹원 목록")
    @GetMapping("users/{name}")
    public ResponseEntity<List<UserGroupResponseDto>> getGroupUserList(@PathVariable String name) throws Exception {
        return ResponseEntity.ok(UserGroupResponseDto.of(groupService.getGroupMemberList(name)));
    }

    @ApiOperation("내 그룹들 정보 조회")
    @GetMapping("/{userid}")
    public ResponseEntity <List<GroupResponseDto.Simple>> getMyGroup(@PathVariable Integer userid) throws Exception {
        return ResponseEntity.ok(GroupResponseDto.Simple.of(groupService.getMyGroupList(userid)));
    }
}
