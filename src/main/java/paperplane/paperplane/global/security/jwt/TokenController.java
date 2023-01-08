package paperplane.paperplane.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/token/expired")
    public String auth() {
        throw new RuntimeException();
    }

    //access 토큰 만료시 refresh 토큰을 통해 재발급
    @GetMapping("/token/refresh")
    public ResponseEntity<String> refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        Token newToken = tokenService.refresh(request, response);

        //기존 쿠키 제거
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("refreshToken")){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        Cookie cookie = new Cookie("refreshToken", newToken.getRefreshToken());
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(newToken.getAccessToken());
    }



}
