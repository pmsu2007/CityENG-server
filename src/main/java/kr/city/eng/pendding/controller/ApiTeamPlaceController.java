package kr.city.eng.pendding.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import kr.city.eng.pendding.dto.TeamPlace;
import kr.city.eng.pendding.dto.TeamPlaceDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams/{teamId}")
public class ApiTeamPlaceController {

  @GetMapping(value = "/places", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<TeamPlace> getPlaces(@PathVariable Long teamId) {
    // TODO: 전체조회
    return Lists.newArrayList();
  }

  @GetMapping(value = "/teams/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<TeamPlace> getTeamPage(@PageableDefault Pageable pageable) {
    // return service.getTeamsWithPage(pageable);
    return null;
  }

  @GetMapping(value = "/places/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamPlace getPlace(@PathVariable Long teamId, @PathVariable Long id) {
    // TODO: 조회
    return null;
  }

  @PostMapping(value = "/place", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamPlace createTeam(@PathVariable Long teamId, @RequestBody TeamPlaceDto team) {
    // TODO: 생성
    team.validate();
    return null;
  }

  @PutMapping(value = "/places/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamPlace updateTeam(@PathVariable Long teamId, @PathVariable Long id, @RequestBody TeamPlaceDto team) {
    // TODO: 수정
    team.validate();
    return null;
  }

  @DeleteMapping(value = "/places/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamPlace deleteTeam(@PathVariable Long teamId, @PathVariable Long id) {
    // TODO: 삭제
    return null;
  }

}
