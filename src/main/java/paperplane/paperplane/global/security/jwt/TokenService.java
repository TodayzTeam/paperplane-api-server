package paperplane.paperplane.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private Key key;
    private final long ACCESS_EXPIRE = 1000 * 60 * 60;             //60분
    private final long REFRESH_EXPIRE = 1000 * 60 * 60 * 24 * 7;   //7일

    @PostConstruct
    protected void init(){
        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public Claims generateClaims(String uid, String role){
        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);
        return claims;
    }

    public Token generateToken(String uid, String role){
        Date issueDate = new Date(); //토큰 발행 시각
        return new Token(
                Jwts.builder()
                        .setClaims(generateClaims(uid, role))
                        .setIssuedAt(issueDate)
                        .setExpiration(new Date(issueDate.getTime() + ACCESS_EXPIRE))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact(),
                Jwts.builder()
                        .setClaims(generateClaims(uid, role))
                        .setIssuedAt(issueDate)
                        .setExpiration(new Date(issueDate.getTime() + REFRESH_EXPIRE))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact()
        );
    }

    public boolean verifyToken(String token){
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build().parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e){
            return false;
        }
    }

    @Transactional
    public Token refresh(HttpServletRequest request, HttpServletResponse response){
        String accessToken = request.getHeader("accessToken");
        String refreshToken = getRefreshTokenFromCookie(request)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh token이 없습니다."));

        //access token에서 user 가져오기
        String email = getUid(accessToken);
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 회원이 없습니다."));

        //refresh token이 유효한지 확인 후 db에 있는 것과 같은지 비교
        if(!verifyToken(refreshToken) || !user.getRefreshToken().equals(refreshToken)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh token이 유효하지 않습니다.");
        }

        //토큰 재발급
        Token newToken = generateToken(email, "USER");

        return newToken;
    }

    public Optional<String> getRefreshTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("refreshToken")){
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    public String getUid(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
