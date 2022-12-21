package paperplane.paperplane.global.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import paperplane.paperplane.domain.user.repository.UserRepository;
import paperplane.paperplane.global.security.OAuth2SuccessHandler;
import paperplane.paperplane.global.security.UserOAuth2Service;
import paperplane.paperplane.global.security.jwt.JwtAuthFilter;
import paperplane.paperplane.global.security.jwt.TokenService;

import static java.util.List.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final UserOAuth2Service userOAuth2Service;
    private final OAuth2SuccessHandler successHandler;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.httpBasic().disable()
                .cors().configurationSource(request -> {
                    var cors = new CorsConfiguration();
                    cors.setAllowedOrigins(of("http://20.38.46.151:3000","http://localhost:3000","http://localhost:8080"));
                    cors.setAllowedMethods(of("GET","POST", "PUT", "DELETE", "OPTIONS"));
                    cors.setAllowedHeaders(of("*"));
                    cors.setAllowCredentials(true);
                    return cors;
                })
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()// 임시 설정
                .and()
                .oauth2Login().loginPage("/token/expired")
                .successHandler(successHandler)
                .userInfoEndpoint().userService(userOAuth2Service);

        return http.addFilterBefore(new JwtAuthFilter(tokenService, userRepository), UsernamePasswordAuthenticationFilter.class).build();
    }
}
