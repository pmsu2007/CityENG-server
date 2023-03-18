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

import kr.city.eng.pendding.dto.Team;
import kr.city.eng.pendding.dto.TeamDto;
import kr.city.eng.pendding.service.ApiTeamService;
import kr.city.eng.pendding.store.entity.enums.UserRole;
import kr.city.eng.pendding.util.AppUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class ApiTeamController {

  private final ApiTeamService service;

  private void verifyRole() {
    Authentication user = AppUtil.getCurrentAuthentication();
    boolean isAdmin = user.getAuthorities().stream()
        .anyMatch(it -> it.getAuthority().equals(UserRole.ADMIN.name()));
    if (!isAdmin) {
      throw new AccessDeniedException("Access Denied");
    }
  }

  // TODO: TeamUser 설정 만들어야 함.

  @PostMapping(value = "/team", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.CREATED)
  public Team createTeam(@RequestBody TeamDto dto) {
    verifyRole();
    dto.validate();
    return service.createOrThrow(dto);
  }

  @GetMapping(value = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Team> getTeams() {
    return service.getEntities();
  }

  @GetMapping(value = "/teams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Team getTeam(@PathVariable Long id) {
    return service.getOrThrow(id);
  }

  @PutMapping(value = "/teams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Team updateTeam(@PathVariable Long id, @RequestBody TeamDto dto) {
    verifyRole();
    dto.validate();
    return service.updateOrThrow(id, dto);
  }

  @DeleteMapping(value = "/teams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteTeam(@PathVariable Long id) {
    verifyRole();
    service.deleteOrThrow(id);
  }

}
