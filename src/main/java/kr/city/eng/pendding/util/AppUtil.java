package kr.city.eng.pendding.util;

import javax.crypto.SecretKey;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.city.eng.pendding.store.entity.enums.UserRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppUtil {

  private static SecretKey jwtSecret = Keys.hmacShaKeyFor("$city@E&G$".getBytes());

  public static SecretKey jwt() {
    return jwtSecret;
  }

  public static Authentication getCurrentAuthentication() {
    SecurityContext context = SecurityContextHolder.getContext();
    if (context == null)
      return null;
    return context.getAuthentication();
  }

  public static Object getCurrentDetails() {
    Authentication auth = getCurrentAuthentication();
    if (auth == null)
      return null;
    return auth.getPrincipal();
  }

  public static String getAuthUser() {
    Object details = getCurrentDetails();
    if (details instanceof UserDetails) {
      return ((UserDetails) details).getUsername();
    }
    return null;
  }

  public static String genApiKey(String username, UserRole role) {
    try {
      return Jwts.builder()
          .setClaims(getClaims(username, role))
          .signWith(jwtSecret).compact();
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  public static Claims getClaims(String username, UserRole role) {
    Claims claims = Jwts.claims();
    claims.put("id", username);
    claims.put("role", role);
    return claims;
  }

}
