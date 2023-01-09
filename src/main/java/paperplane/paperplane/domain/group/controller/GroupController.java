package paperplane.paperplane.domain.group.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import paperplane.paperplane.domain.group.dto.GroupRequestDto;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;
import paperplane.paperplane.domain.group.service.GroupService;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.dto.UserResponseDto;
import paperplane.paperplane.domain.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Group Controller"})
@Slf4j
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;

    @ApiOperation("그룹 생성")
    @PostMapping("/create")
    public ResponseEntity<Integer> createGroup(GroupRequestDto.Create create) throws Exception {
        return ResponseEntity.ok(groupService.createGroup(create, userService.getLoginUser()));
    }

    @ApiOperation("그룹 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteGroup(@Valid GroupRequestDto.GroupCode groupCode) throws Exception {
        //  groupService.deleteGroup(groupCode, email);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("그룹 요약 정보 조회")
    @GetMapping("/simple/{id}")
    public ResponseEntity<GroupResponseDto.Simple> getGroupInfo(@PathVariable Integer id) throws Exception {
        return ResponseEntity.ok(GroupResponseDto.Simple.of(groupService.getGroupById(id)));
    }

    @ApiOperation("그룹 참가")
    @PostMapping("/join")
    public ResponseEntity<GroupResponseDto.Info> joinGroup(@Valid GroupRequestDto.GroupCode groupCode) throws Exception {
        return ResponseEntity.ok(GroupResponseDto.Info.of(groupService.joinGroup(groupCode, userService.getLoginUser())));
    }

    @ApiOperation("그룹 탈퇴")
    @PatchMapping("/resign")
    public ResponseEntity<Void> resignGroup(@Valid GroupRequestDto.GroupCode groupCode) throws Exception {
        groupService.resignGroup(groupCode, userService.getLoginUser());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("그룹을 그룹명으로 검색")
    @GetMapping("search/{name}")
    public ResponseEntity<List<GroupResponseDto.Simple>> findGroup(@PathVariable String name) throws Exception {
        return ResponseEntity.ok(GroupResponseDto.Simple.of(groupService.getGroupListByName(name)));
    }

    @ApiOperation("해당 그룹의 그룹원 목록")
    @GetMapping("users/{name}")
    public ResponseEntity<PageImpl<UserResponseDto.Simple>> getGroupUserList(@PathVariable String name, @RequestParam("page") Integer page) throws Exception {
        PageRequest pageRequest = PageRequest.of(page, 6);
        return ResponseEntity.ok(groupService.getGroupMemberListByName(name, pageRequest));
    }

    @ApiOperation("내 그룹들 정보 조회")
    @GetMapping("/{userid}")
    public ResponseEntity <List<GroupResponseDto.Simple>> getMyGroup(@PathVariable Integer userid) throws Exception {
        return ResponseEntity.ok(GroupResponseDto.Simple.of(groupService.getMyGroupList(userid)));
    }
}
