package kr.city.eng.pendding.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.city.eng.pendding.dto.TeamRole;
import kr.city.eng.pendding.dto.TeamRoleDto;
import kr.city.eng.pendding.service.ApiTeamPermission;
import kr.city.eng.pendding.service.ApiTeamRoleService;
import kr.city.eng.pendding.store.entity.enums.TeamPermission;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams")
public class ApiTeamRoleController {

  private final ApiTeamRoleService service;
  private final ApiTeamPermission teamPermission;

  @PostMapping(value = "/{teamId}/role", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.CREATED)
  public TeamRole createTeamPlace(@PathVariable Long teamId, @RequestBody TeamRoleDto dto) {
    teamPermission.verify(teamId, TeamPermission.ROLE);
    dto.validate();
    return service.createOrThrow(teamId, dto);
  }

  @PutMapping(value = "/roles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamRole updateTeamPlace(@PathVariable Long id, @RequestBody TeamRoleDto dto) {
    // TODO: teamId 추가해야 함.
    dto.validate();
    return service.updateOrThrow(id, dto);
  }

  @DeleteMapping(value = "/roles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteTeamPlace(@PathVariable Long id) {
    // TODO: teamId 추가해야 함.
    service.deleteOrThrow(id);
  }

  @GetMapping(value = "/{teamId}/roles", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<TeamRole> getTeamPlaces(@PathVariable Long teamId) {
    return service.getEntities(teamId);
  }

  @GetMapping(value = "/roles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamRole getTeamPlace(@PathVariable Long id) {
    return service.getOrThrow(id);
  }

}
