package paperplane.paperplane.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@NoArgsConstructor
@Service
public class TokenService {

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

    public String getUid(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
