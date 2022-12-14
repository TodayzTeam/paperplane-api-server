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
    @PostMapping("/interest/{id}")
    public ResponseEntity<Void> addInterest(@PathVariable Integer id, String keyword) throws ParseException {
        userService.addInterest(id, keyword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("내 전체 정보 조회")
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserResponseDto.Info> getMyProfile(@PathVariable Integer id) throws Exception {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserResponseDto.Info.of(user));
    }

    @ApiOperation("액세스 토큰으로 유저 아이디 반환")
    @GetMapping("/token")
    public ResponseEntity<Integer> getMyProfileByToken(@RequestHeader(name = "accessToken") String accessToken){
        return ResponseEntity.ok(userService.getUserByToken(accessToken));
    }

    @ApiOperation("내 요약 정보 조회")
    @GetMapping("/simple/{id}")
    public ResponseEntity<UserResponseDto.Simple> getMySimple(@PathVariable Integer id) throws Exception {
        User user = userService.getUserById(id);

        return ResponseEntity.ok(UserResponseDto.Simple.of(user));
    }

    @ApiOperation("내 정보 수정")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDto.Info> updateProfile(@Valid UserRequestDto.Profile profile, @PathVariable Integer id) throws Exception {
        User user = userService.updateUser(id, profile);

        return ResponseEntity.ok(UserResponseDto.Info.of(user));
    }

    @ApiOperation("프로필 이미지 변경")
    @PutMapping("/update/image/{id}")
    public ResponseEntity<String> updateProfileImage(@PathVariable Integer id, @RequestParam MultipartFile file) throws Exception {
        return ResponseEntity.ok(userService.updateProfileImage(id, file));
    }

    @ApiOperation("로그아웃")
    @PostMapping("/logout/{id}")
    public ResponseEntity<Void> logout(@PathVariable Integer id){
        User user = userService.getUserById(id);
        user.setRefreshToken(null);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("탈퇴")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> updateProfile(@PathVariable Integer id) throws Exception {
        userService.deleteUser(userService.getUserById(id));

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ApiOperation("모든 user Id만 반환")
    @GetMapping("/allid")
    public ResponseEntity<List<Integer>> allUserId() throws Exception {
        return ResponseEntity.ok(userService.getAllUserId());
    }

}
