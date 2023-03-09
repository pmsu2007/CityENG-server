package kr.city.eng.pendding.dto.auth;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;

import kr.city.eng.pendding.store.entity.enums.TeamPermission;
import kr.city.eng.pendding.store.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamGrantedAuthority implements GrantedAuthority {

  private static final long serialVersionUID = -6983980256471948549L;

  private UserRole role;
  private Set<TeamPermission> permissions;

  @Override
  public String getAuthority() {
    return "ROLE_" + this.getRole();
  }

  public String getRole() {
    return this.role.name();
  }

  public String getPermissionAbbrs() {
    return permissions.stream().map(item -> item.getAbbr())
        .collect(Collectors.joining(":"));
  }

  public boolean containPermission(TeamPermission permission) {
    return permissions.stream()
        .anyMatch(predicate -> predicate.equals(permission));
  }

  public static TeamGrantedAuthority of(String roleName, String abbrPermision) {
    TeamGrantedAuthority entity = new TeamGrantedAuthority();

    Set<TeamPermission> permissions = Stream.of(abbrPermision.split(":"))
        .map(TeamPermission::abbrOf)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    entity.setRole(UserRole.valueOf(roleName));
    entity.setPermissions(permissions);
    return entity;
  }

  public static TeamGrantedAuthority admin() {
    return new TeamGrantedAuthority(UserRole.ADMIN, TeamPermission.admin());
  }

  public static TeamGrantedAuthority member() {
    return new TeamGrantedAuthority(UserRole.USER, TeamPermission.member());
  }

}
