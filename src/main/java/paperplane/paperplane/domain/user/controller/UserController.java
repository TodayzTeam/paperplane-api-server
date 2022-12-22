package paperplane.paperplane.domain.user.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.Interest.dto.InterestResponseDto;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;
import paperplane.paperplane.domain.postinterest.dto.PostInterestResponseDto;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.dto.UserRequestDto;
import paperplane.paperplane.domain.user.dto.UserResponseDto;
import paperplane.paperplane.domain.user.service.UserService;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.usergroup.dto.UserGroupResponseDto;
import paperplane.paperplane.global.security.jwt.TokenService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Api(tags = {"User Controller"})
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @ApiOperation("내 전체 정보 조회")
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserResponseDto.Info> getMyProfile(@PathVariable Integer id) throws Exception {
        return ResponseEntity.ok(UserResponseDto.Info.builder()
                .id(1)
                .name("user")
                .email("abc@naver.com")
                .profileImageUrl("sddsf")
                .isPopularLetterEmail(false)
                .isReadEmail(false)
                .isRepliedEmail(false)
                .isPopularLetterWeb(false)
                .isRepliedWeb(false)
                .isReadWeb(false)
                .userGroups(new ArrayList<>())
                .userInterests(new ArrayList<>())
                .build());
    }
    @ApiOperation("내 요약 정보 조회")
    @GetMapping("/simple/{id}")
    public ResponseEntity<UserResponseDto.Simple> getMySimple(@PathVariable Integer id) throws Exception {
        return ResponseEntity.ok(UserResponseDto.Simple.builder()
                .name("user")
                .email("abc@naver.com")
                .profileImageUrl("sddsf")
                .build());
    }

    @ApiOperation("내 정보 수정")
    @PostMapping("/update/{id}")
    public ResponseEntity<UserResponseDto.Info> updateProfile(@Valid UserRequestDto.Profile profile, @PathVariable Integer id) throws Exception {
        return ResponseEntity.ok(UserResponseDto.Info.builder()
                .id(1)
                .name(profile.getName())
                .email("abc@naver.com")
                .profileImageUrl("sddsf")
                .isPopularLetterEmail(false)
                .isReadEmail(false)
                .isRepliedEmail(false)
                .isPopularLetterWeb(false)
                .isRepliedWeb(false)
                .isReadWeb(false)
                .userGroups(new ArrayList<>())
                .userInterests(new ArrayList<>())
                .build());
    }

    @ApiOperation("로그아웃")
    @PostMapping("/logout/{id}")
    public ResponseEntity<Void> logout(@PathVariable Integer id){
        User user = userService.getUser(id);
        user.setRefreshToken(null);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("탈퇴")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> updateProfile(@PathVariable Integer id) throws Exception {
        userService.deleteUser(userService.getUser(id));

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
