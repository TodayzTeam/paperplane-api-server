package paperplane.paperplane.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @GetMapping("/token/expired")
    public String auth() {
        throw new RuntimeException();
    }

    //토큰 만료시 재발급
    @GetMapping("/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader("refreshToken");

        if (refreshToken != null && tokenService.verifyToken(refreshToken)) {
            String email = tokenService.getUid(refreshToken);
            Token newToken = tokenService.generateToken(email, "USER");

            Optional<User> user = userRepository.findByEmail(email);
            user.get().setRefreshToken(newToken.getRefreshToken());

            response.addHeader("accessToken", newToken.getAccessToken());
            response.addHeader("refreshToken", newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");

            return "HAPPY NEW TOKEN";
        }

        throw new RuntimeException();
    }



}
