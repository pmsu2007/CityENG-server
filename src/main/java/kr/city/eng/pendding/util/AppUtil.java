package kr.city.eng.pendding.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppUtil {

  public static Object getCurrentDetails() {
    SecurityContext context = SecurityContextHolder.getContext();
    if (context == null)
      return null;
    Authentication auth = context.getAuthentication();
    if (auth == null)
      return null;

    return auth.getPrincipal();
  }

  public static String getAuthUser() {
    Object details = getCurrentDetails();
    if (details instanceof UserDetails) {
      return ((UserDetails) details).getUsername();
    }
    // FIXME: 실제 유정정보를 활용해야 함.
    return "admin";
  }

}
