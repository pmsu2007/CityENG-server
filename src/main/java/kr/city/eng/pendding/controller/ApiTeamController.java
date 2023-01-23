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

import kr.city.eng.pendding.dto.Team;
import kr.city.eng.pendding.dto.TeamDto;
import kr.city.eng.pendding.service.ApiTeamService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class ApiTeamController {

  private final ApiTeamService service;

  @GetMapping(value = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Team> getTeams() {
    return service.getTeams();
  }

  @GetMapping(value = "/teams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Team getTeam(@PathVariable Long id) {
    return service.getTeamOrThrow(id);
  }

  @PostMapping(value = "/team", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.CREATED)
  public Team createTeam(@RequestBody TeamDto team) {
    team.validate();
    return service.createOrThrow(team);
  }

  @PutMapping(value = "/teams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Team updateTeam(@PathVariable Long id, @RequestBody TeamDto team) {
    team.validate();
    return service.updateOrThrow(id, team);
  }

  @DeleteMapping(value = "/teams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteTeam(@PathVariable Long id) {
    service.deleteOrThrow(id);
  }

}
