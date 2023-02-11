package kr.city.eng.pendding.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;

import kr.city.eng.pendding.dto.TeamPending;
import kr.city.eng.pendding.service.ApiTeamPendingService;
import kr.city.eng.pendding.store.entity.enums.PendingType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams")
public class ApiTeamPendingController {

  private final ApiTeamPendingService service;

  @GetMapping(value = "/{teamId}/pendings", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<TeamPending> getTeamPendings(@PathVariable Long teamId,
      @RequestParam(required = false) String userId,
      @RequestParam(required = false) Long startDateTime,
      @RequestParam(required = false) Long endDateTime,
      @RequestParam(required = false) Set<String> types,
      @RequestParam(required = false) Set<Long> placeIds) {
    Set<PendingType> pendingTypes = Sets.newHashSet();
    if (!ObjectUtils.isEmpty(types)) {
      types.forEach(it -> pendingTypes.add(PendingType.valueOf(it)));
    }
    return service.getEntities(teamId, userId, startDateTime, endDateTime, pendingTypes, placeIds);
  }

  @GetMapping(value = "/pendings/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamPending getTeamPending(@PathVariable Long id) {
    return service.getOrThrow(id);
  }

  @PostMapping(value = "/{teamId}/pending", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamPending setPendingType(@PathVariable Long teamId, @RequestBody TeamPending dto) {
    dto.setId(null);
    dto.validate();
    return service.setPendingType(teamId, dto);
  }

  @DeleteMapping(value = "/pendings/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteTeamPending(@PathVariable Long id) {
    service.deleteOrThrow(id);
  }

}
