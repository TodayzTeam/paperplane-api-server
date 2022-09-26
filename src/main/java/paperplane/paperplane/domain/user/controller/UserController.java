package paperplane.paperplane.domain.user.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paperplane.paperplane.domain.user.service.UserService;

@Api(tags = {"User Controller"})
@Slf4j
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
