package kr.city.eng.pendding.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.city.eng.pendding.dto.TeamUser;
import kr.city.eng.pendding.service.ApiTeamUserService;
import kr.city.eng.pendding.store.entity.enums.UserRole;
import kr.city.eng.pendding.util.AppUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams/{teamId}")
public class ApiTeamUserController {

  private final ApiTeamUserService service;

  private void verifyRole() {
    Authentication user = AppUtil.getCurrentAuthentication();
    boolean isAdmin = user.getAuthorities().stream()
        .anyMatch(it -> it.getAuthority().equals(UserRole.ADMIN.name()));
    if (!isAdmin) {
      throw new AccessDeniedException("Access Denied");
    }
  }

  @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.CREATED)
  public TeamUser createTeamUser(@PathVariable Long teamId,
      @RequestBody TeamUser dto) {
    verifyRole();
    dto.validate();
    return service.createTeamUserOrThrow(teamId, dto);
  }

  @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamUser updateTeamUser(@PathVariable Long teamId,
      @RequestBody TeamUser dto) {
    verifyRole();
    dto.validate();
    return service.updateTeamUserOrThrow(teamId, dto);
  }

  @DeleteMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteTeamUser(@PathVariable Long teamId,
      @PathVariable String userId) {
    verifyRole();
    service.deleteTeamUserOrThrow(teamId, userId);
  }

  @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public List<TeamUser> getTeamUsers(@PathVariable Long teamId) {
    return service.getTeamUsers(teamId);
  }

}
