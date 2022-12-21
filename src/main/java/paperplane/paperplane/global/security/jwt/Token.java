package paperplane.paperplane.global.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Token {
    private String accessToken;
    private String refreshToken;

}
