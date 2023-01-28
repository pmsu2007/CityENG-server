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
import kr.city.eng.pendding.store.repo.TbTeamPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiTeamPlaceService {

  private final TbTeamPlaceRepo storePlace;
  private final TbTeamPlaceMapper palceMapper;

  private final TbTeamRepo storeTeam;

  private TbTeamPlace findByIdOrThrow(Long id) {
    return storePlace.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamPlace.class.getName()));
  }

  private TbTeam findTeamOrThrow(Long teamId) {
    return storeTeam.findById(teamId)
        .orElseThrow(() -> ExceptionUtil.id(teamId, TbTeam.class.getName()));
  }

  @Transactional
  public List<TeamPlace> getEntities(Long teamId) {
    TbTeam team = findTeamOrThrow(teamId);
    return storePlace.findByTeam(team).stream()
        .map(palceMapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public Page<TeamPlace> getEntities(Long teamId, Pageable pageable) {
    TbTeam team = findTeamOrThrow(teamId);
    return storePlace.findByTeam(team, pageable).map(palceMapper::toDto);
  }

  @Transactional
  public TeamPlace createOrThrow(Long teamId, TeamPlaceDto dto) {
    TbTeamPlace entity = palceMapper.toEntity(dto);
    entity.setTeam(findTeamOrThrow(teamId));
    return palceMapper.toDto(storePlace.save(entity));
  }

  @Transactional
  public TeamPlace getOrThrow(Long id) {
    return palceMapper.toDto(findByIdOrThrow(id));
  }

  @Transactional
  public TeamPlace updateOrThrow(Long id, TeamPlaceDto team) {
    TbTeamPlace entity = findByIdOrThrow(id);
    palceMapper.updateEntity(entity, team);
    return palceMapper.toDto(storePlace.save(entity));
  }

  @Transactional
  public void deleteOrThrow(Long id) {
    storePlace.deleteById(id);
  }

}
