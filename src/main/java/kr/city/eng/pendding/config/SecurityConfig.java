package kr.city.eng.pendding.config;

/**
 * <pre>
 * 
 * import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
 * import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
 * import org.springframework.context.ApplicationContext;
 * import org.springframework.context.annotation.Bean;
 * import org.springframework.context.annotation.Configuration;
 * import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 * import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
 * import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
 * import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 * import org.springframework.security.web.SecurityFilterChain;
 * import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 * 
 * import lombok.RequiredArgsConstructor;
 * 
 * &#64;RequiredArgsConstructor
 * &#64;ConditionalOnWebApplication(type = Type.SERVLET)
 * &#64;Configuration
 * &#64;EnableWebSecurity
 * public class SecurityConfig implements WebMvcConfigurer {
 * 
 *   private final ApplicationContext context;
 * 
 *   &#64;Bean
 *   public BCryptPasswordEncoder passwordEncoder() {
 *     return new BCryptPasswordEncoder();
 *   }
 * 
 *   &#64;Bean
 *   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 *     http.authorizeRequests().anyRequest().permitAll();
 *     http.authorizeRequests().antMatchers("/login").permitAll()
 *         .antMatchers("/users/**", "/settings/**").hasAuthority("Admin")
 *         .hasAnyAuthority("Admin", "Editor", "Salesperson")
 *         .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
 *         .anyRequest().authenticated()
 *         .and().formLogin()
 *         .loginPage("/login")
 *         .usernameParameter("email")
 *         .permitAll()
 *         .and()
 *         .rememberMe().key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
 *         .and()
 *         .logout().permitAll();
 * 
 *     http.headers().frameOptions().sameOrigin();
 * 
 *     return http.build();
 *   }
 * 
 *   @Bean
 *   public WebSecurityCustomizer webSecurityCustomizer() {
 *     return web -> web.ignoring().antMatchers("/images/**", "/js/**");
 *   }
 * 
 * }
 * 
 * </pre>
 **/