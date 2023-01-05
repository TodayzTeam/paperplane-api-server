package paperplane.paperplane.domain.group.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.dto.GroupRequestDto;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;
import paperplane.paperplane.domain.group.service.GroupService;
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
    @GetMapping("/create")
    public ResponseEntity<GroupResponseDto.Info> createGroup() throws Exception {
        return ResponseEntity.ok(GroupResponseDto.Info.builder()
                .id(1)
                .name("groupName")
                .code("12345abcd")
                .userGroupResponseDtoList(new ArrayList<>())
                .build());
    }
    @ApiOperation("그룹 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteGroup(@Valid GroupRequestDto.GroupCode groupCode ) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("그룹 참가")
    @PostMapping("/join")
    public ResponseEntity<GroupResponseDto.Info> joinGroup(@Valid GroupRequestDto.GroupCode groupCode ) throws Exception {
        return ResponseEntity.ok(GroupResponseDto.Info.builder()
                .id(1)
                .name("groupName")
                .code("12345abcd")
                .userGroupResponseDtoList(new ArrayList<>())
                .build());
    }
    @ApiOperation("그룹 탈퇴")
    @PatchMapping("resign")
    public ResponseEntity<Void> resignGroup(@Valid GroupRequestDto.GroupCode groupCode ) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("그룹을 그룹명으로 검색")
    @GetMapping("search/{name}")
    public ResponseEntity<List<GroupResponseDto.Simple>> resignGroup(@PathVariable String name) throws Exception {
        List<GroupResponseDto.Simple> simpleList = new ArrayList<>();
        simpleList.add(GroupResponseDto.Simple.builder()
                .id(1)
                .name("group1")
                .code("asdfq3").build());
        simpleList.add(GroupResponseDto.Simple.builder()
                .id(2)
                .name("group2")
                .code("dsjkew3").build());

        return ResponseEntity.ok(simpleList);
    }

    @ApiOperation("해당 그룹의 그룹원 목록")
    @GetMapping("users/{name}")
    public ResponseEntity<List<UserGroupResponseDto>> getGroupUserList(@PathVariable String name) throws Exception {
        List<UserGroupResponseDto> simpleList = new ArrayList<>();
        simpleList.add(UserGroupResponseDto.builder()
                .groupId(1)
                .groupName("asdfq3").build());
        simpleList.add(UserGroupResponseDto.builder()
                .groupId(1)
                .groupName("asdfq3").build());

        return ResponseEntity.ok(simpleList);
    }

    @ApiOperation("내 그룹들 정보 조회")
    @GetMapping("/group/{userid}")
    public ResponseEntity <List<GroupResponseDto.Simple>> getMyGroup(@PathVariable Integer userid) throws Exception {
        List<GroupResponseDto.Simple> simpleList= new ArrayList<>();
        simpleList.add(GroupResponseDto.Simple.of(Group.builder()
                .name("group1")
                .code("abcd")
                .build()));
        simpleList.add(GroupResponseDto.Simple.of(Group.builder()
                .name("group2")
                .code("efgh")
                .build()));
        return ResponseEntity.ok(simpleList);
    }


}
