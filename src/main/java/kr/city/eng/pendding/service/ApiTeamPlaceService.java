package kr.city.eng.pendding.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.city.eng.pendding.dto.TeamPlace;
import kr.city.eng.pendding.dto.TeamPlaceDto;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamPlace;
import kr.city.eng.pendding.store.mapper.TbTeamPlaceMapper;
import kr.city.eng.pendding.store.repo.TbTeamPendingProdRepo;
import kr.city.eng.pendding.store.repo.TbTeamPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiTeamPlaceService {

  private final TbTeamPlaceRepo store;
  private final TbTeamProdPlaceRepo storeProdPlace;
  private final TbTeamPlaceMapper mapper;

  private final TbTeamRepo storeTeam;
  private final TbTeamPendingProdRepo storePendingProd;

  private TbTeamPlace findByIdOrThrow(Long id) {
    return store.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamPlace.class.getName()));
  }

  private TbTeam findTeamOrThrow(Long teamId) {
    return storeTeam.findById(teamId)
        .orElseThrow(() -> ExceptionUtil.id(teamId, TbTeam.class.getName()));
  }

  @Transactional
  public List<TeamPlace> getEntities(Long teamId) {
    TbTeam team = findTeamOrThrow(teamId);
    return store.findByTeam(team).stream()
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public Page<TeamPlace> getEntities(Long teamId, Pageable pageable) {
    TbTeam team = findTeamOrThrow(teamId);
    return store.findByTeam(team, pageable).map(mapper::toDto);
  }

  @Transactional
  public TeamPlace createOrThrow(Long teamId, TeamPlaceDto dto) {
    TbTeamPlace entity = mapper.toEntity(dto);
    entity.setTeam(findTeamOrThrow(teamId));
    return mapper.toDto(store.save(entity));
  }

  @Transactional
  public TeamPlace getOrThrow(Long id) {
    return mapper.toDto(findByIdOrThrow(id));
  }

  @Transactional
  public TeamPlace updateOrThrow(Long id, TeamPlaceDto team) {
    TbTeamPlace entity = findByIdOrThrow(id);
    mapper.updateEntity(entity, team);
    return mapper.toDto(store.save(entity));
  }

  @Transactional
  public void deleteOrThrow(Long id) {
    storePendingProd.setToPlaceNull(id);
    storePendingProd.setFromPlaceNull(id);

    storeProdPlace.deleteByPlaceId(id);
    store.deleteById(id);
  }

}
