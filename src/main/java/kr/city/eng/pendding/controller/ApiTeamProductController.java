package kr.city.eng.pendding.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.dto.TeamProductDto;
import kr.city.eng.pendding.dto.TeamProductDto.Place;
import kr.city.eng.pendding.service.ApiTeamProductService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teams")
public class ApiTeamProductController {

  private final ApiTeamProductService service;

  @PostMapping(value = "/{teamId}/product", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.CREATED)
  public TeamProduct createTeamProduct(@PathVariable Long teamId, @RequestBody TeamProductDto dto) {
    dto.validate();
    return service.createOrThrow(teamId, dto);
  }

  @GetMapping(value = "/{teamId}/products/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<TeamProduct> getTeamProducts(@PathVariable Long teamId,
      @RequestParam(required = false) String value,
      @PageableDefault(size = 10) Pageable pageable) {
    return service.getEntities(teamId, value, pageable);
  }

  @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamProduct getTeamProduct(@PathVariable Long id) {
    return service.getOrThrow(id);
  }

  @GetMapping(value = "/products/{id}/places/{placeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Place getTeamProductPlace(@PathVariable Long id, @PathVariable Long placeId) {
    return service.getTeamProductPlaceOrThrow(id, placeId);
  }

  @PatchMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeamProduct patchTeamProduct(@PathVariable Long id, @RequestBody TeamProductDto dto) {
    dto.validatePatch();
    return service.patchOrThrow(id, dto);
  }

  @DeleteMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteTeamProduct(@PathVariable Long id) {
    service.deleteOrThrow(id);
  }

}
