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

import java.util.List;

import static java.util.List.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    /*private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;*/
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
                .anyRequest().permitAll();// 임시 설정
                //.antMatchers("/token/**").permitAll()
                //.anyRequest().authenticated()

                /*.and()
                .addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                .loginPage("/token/expired")
                .successHandler(successHandler)
                .userInfoEndpoint().userService(oAuth2UserService);*/

        return http.build();
    }


}
