package kr.city.eng.pendding.config.secure;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import kr.city.eng.pendding.util.AppUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  public boolean validateToken(final String token) {
    getClaims(token);
    return true;
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String token) {
    try {
      Claims claims = getClaims(token);
      String principal = (String) claims.get("id");
      GrantedAuthority authority = new SimpleGrantedAuthority((String) claims.get("role"));
      return new UsernamePasswordAuthenticationToken(principal, null, Sets.newHashSet(authority));
    } catch (ExpiredJwtException e) {
      throw new BadCredentialsException("Expired Token Access");
    } catch (ClaimJwtException e) {
      throw new BadCredentialsException("Bad Token Refresh");
    } catch (Exception e) {
      throw new BadCredentialsException("Unauthorized", e);
    }
  }

  @SuppressWarnings("squid:S1168")
  public Claims getClaimsByAccessToken(final String token) {
    // 토큰이 만료 되었는지 확인
    Claims claims = getClaims(token);
    if (!claims.containsKey("id"))
      return null;
    if (!claims.containsKey("role"))
      return null;
    return claims;
  }

  public Claims getClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(AppUtil.jwt())
        .build().parseClaimsJws(token).getBody();
  }

  public Claims getUserClaims(UserDetails user) {
    GrantedAuthority authority = user.getAuthorities().stream().findFirst().orElseThrow();

    Claims claims = Jwts.claims();
    claims.put("id", user.getUsername());
    claims.put("role", authority.getAuthority());
    return claims;
  }

}
