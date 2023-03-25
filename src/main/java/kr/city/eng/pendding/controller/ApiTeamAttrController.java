package kr.city.eng.pendding.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import kr.city.eng.pendding.dto.TeamAttr;
import kr.city.eng.pendding.dto.TeamAttrDto;
import kr.city.eng.pendding.service.ApiTeamAttrService;
import kr.city.eng.pendding.service.ApiTeamPermission;
import kr.city.eng.pendding.store.entity.enums.TeamPermission;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams")
public class ApiTeamAttrController {

  private final ApiTeamAttrService service;
  private final ApiTeamPermission teamPermission;

  @PostMapping(value = "/{teamId}/attr", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.CREATED)
  public TeamAttr createTeamAttr(@PathVariable Long teamId, @RequestBody TeamAttrDto dto) {
    teamPermission.verify(teamId, TeamPermission.ATTRIBUTE);
    dto.validate();
    return service.createOrThrow(teamId, dto);
  }

  @PutMapping(value = "/{teamId}/attrs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamAttr updateTeamAttr(@PathVariable Long teamId,
      @PathVariable Long id, @RequestBody TeamAttrDto dto) {
    teamPermission.verify(teamId, TeamPermission.ATTRIBUTE);
    dto.validate();
    return service.updateOrThrow(id, dto);
  }

  @DeleteMapping(value = "/{teamId}/attrs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteTeamAttr(@PathVariable Long teamId, @PathVariable Long id) {
    teamPermission.verify(teamId, TeamPermission.ATTRIBUTE);
    service.deleteOrThrow(id);
  }

  @GetMapping(value = "/{teamId}/attrs", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<TeamAttr> getTeamAttrs(@PathVariable Long teamId) {
    return service.getEntities(teamId);
  }

  @GetMapping(value = "/{teamId}/attrs/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<TeamAttr> getTeamAttrs(@PathVariable Long teamId, @PageableDefault Pageable pageable) {
    return service.getEntities(teamId, pageable);
  }

  @GetMapping(value = "/attrs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamAttr getTeamAttr(@PathVariable Long id) {
    return service.getOrThrow(id);
  }

  @GetMapping(value = "/attrs/{id}/values", produces = MediaType.APPLICATION_JSON_VALUE)
  public Collection<String> getTeamAttrValues(@PathVariable Long id) {
    return service.getAttrValues(id);
  }

}
