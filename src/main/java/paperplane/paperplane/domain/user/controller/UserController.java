package paperplane.paperplane.domain.user.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.dto.UserRequestDto;
import paperplane.paperplane.domain.user.dto.UserResponseDto;
import paperplane.paperplane.domain.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"User Controller"})
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiOperation("회원가입 후 관심사 등록")
    @PostMapping("/interest")
    public ResponseEntity<Void> addInterest(String keyword) throws ParseException {
        userService.addInterest(keyword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("내 전체 정보 조회")
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto.Info> getMyProfile() throws Exception {
        User user = userService.getUserById(userService.getLoginUser());
        return ResponseEntity.ok(UserResponseDto.Info.of(user));
    }

    @ApiOperation("내 요약 정보 조회")
    @GetMapping("/simple")
    public ResponseEntity<UserResponseDto.Simple> getMySimple() throws Exception {
        User user = userService.getUserById(userService.getLoginUser());
        return ResponseEntity.ok(UserResponseDto.Simple.of(user));
    }

    @ApiOperation("내 정보 수정")
    @PutMapping("/update")
    public ResponseEntity<UserResponseDto.Info> updateProfile(@Valid UserRequestDto.Profile profile) throws Exception {
        User user = userService.updateUser(profile);
        return ResponseEntity.ok(UserResponseDto.Info.of(user));
    }

    @ApiOperation("프로필 이미지 변경")
    @PutMapping("/update/image")
    public ResponseEntity<String> updateProfileImage(@RequestParam MultipartFile file) throws Exception {
        return ResponseEntity.ok(userService.updateProfileImage(file));
    }

    @ApiOperation("로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        userService.logout();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("탈퇴")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> updateProfile() throws Exception {
        userService.deleteUser();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("모든 user Id만 반환")
    @GetMapping("/allid")
    public ResponseEntity<List<Integer>> allUserId() throws Exception {
        return ResponseEntity.ok(userService.getAllUserId());
    }

}
