package kr.city.eng.pendding.controller;

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

import kr.city.eng.pendding.dto.TeamPlace;
import kr.city.eng.pendding.dto.TeamPlaceDto;
import kr.city.eng.pendding.service.ApiTeamPlaceService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams")
public class ApiTeamPlaceController {

  private final ApiTeamPlaceService service;

  @PostMapping(value = "/{teamId}/place", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.CREATED)
  public TeamPlace createTeam(@PathVariable Long teamId, @RequestBody TeamPlaceDto dto) {
    dto.validate();
    return service.createOrThrow(teamId, dto);
  }

  @GetMapping(value = "/{teamId}/places", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<TeamPlace> getTeamPlaces(@PathVariable Long teamId) {
    return service.getEntities(teamId);
  }

  @GetMapping(value = "/{teamId}/places/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<TeamPlace> getTeamPlaces(@PathVariable Long teamId, @PageableDefault Pageable pageable) {
    return service.getEntities(teamId, pageable);
  }

  @GetMapping(value = "/places/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamPlace getTeamPlace(@PathVariable Long id) {
    return service.getOrThrow(id);
  }

  @PutMapping(value = "/places/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamPlace updateTeam(@PathVariable Long id, @RequestBody TeamPlaceDto dto) {
    dto.validate();
    return service.updateOrThrow(id, dto);
  }

  @DeleteMapping(value = "/places/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteTeam(@PathVariable Long id) {
    service.deleteOrThrow(id);
  }

}
