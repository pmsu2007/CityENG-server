package kr.city.eng.pendding.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ConditionalOnWebApplication(type = Type.SERVLET)
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

  private final ApplicationContext context;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeRequests(requests -> {
      // h2-console 무시
      requests.antMatchers("/h2-console/**").permitAll();

      requests.anyRequest().permitAll();
    });

    // 세션관리 안함.
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // CORS 설정(기본적으로 preflight 요청은 인증 무시)
    // AllowCredentials=true로 설정하면 AllowedOrigin에 '*'는 사용할 수 없음.
    http.cors(cors -> {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");
      config.addAllowedOrigin("*");
      source.registerCorsConfiguration("/api/**", config);
      source.registerCorsConfiguration("/actuator/info", config);
      source.registerCorsConfiguration("/actuator/health", config);
      cors.configurationSource(source);
    });

    // CSRF 설정(세션이 아닌 상태가 없는 JWT토큰으로 사용하므로 끔)
    http.csrf().disable();
    // iframe(X-Frame-Options) 허용
    http.headers().frameOptions().sameOrigin();

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().antMatchers("/images/", "/js/");
  }

}
