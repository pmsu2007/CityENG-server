package kr.city.eng.pendding.config.secure;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 참조
 * org.springframework.security.web.authentication.www.BasicAuthenticationFilter
 */
@RequiredArgsConstructor
@Slf4j
public class WebAuthenticationJwtFilter extends GenericFilterBean {

  public static final String BEARER = "Bearer ";
  private final JwtTokenProvider jwtTokenProvider;
  private final AppAuthenticationEntryPoint authenticationEntryPoint = new AppAuthenticationEntryPoint();
  private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    String accessToken = getJwtAccessToken(request);
    if (accessToken == null) {
      chain.doFilter(request, response);
      return;
    }

    if (authentication(request, response, accessToken)) {
      chain.doFilter(request, response);
    }
  }

  private boolean authentication(HttpServletRequest request, HttpServletResponse response,
      String accessToken) throws IOException, ServletException {
    try {
      AbstractAuthenticationToken authResult = jwtTokenProvider.getAuthentication(accessToken);
      authResult.setDetails(authenticationDetailsSource.buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authResult);
      if (log.isDebugEnabled()) {
        log.debug("Set SecurityContextHolder to: {}", authResult);
      }
      return true;
    } catch (AuthenticationException ex) {
      SecurityContextHolder.clearContext();
      if (log.isDebugEnabled()) {
        log.debug("Failed to process authentication request: {}", ex.getMessage());
      }
      this.authenticationEntryPoint.commence(request, response, ex);
    }
    return false;
  }

  private String getJwtAccessToken(HttpServletRequest request) {
    // Header에 있는지 확인
    String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.startsWithIgnoreCase(authorization, BEARER)) {
      String accessToken = (authorization.length() <= BEARER.length()) ? ""
          : authorization.substring(BEARER.length(), authorization.length());
      if (StringUtils.hasLength(accessToken)) {
        return accessToken;
      }
    }
    return null;
  }

}
