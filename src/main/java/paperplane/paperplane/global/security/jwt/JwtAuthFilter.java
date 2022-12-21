package paperplane.paperplane.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.user.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader("Authorization");

        if(token != null && tokenService.verifyToken(token)){
            String email = tokenService.getUid(token);

            Authentication auth = getAuthentication(userRepository.findByEmail(email).get());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(User user){
        return new UsernamePasswordAuthenticationToken(user, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
