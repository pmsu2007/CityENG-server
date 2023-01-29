package kr.city.eng.pendding.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.city.eng.pendding.dto.TeamAttr;
import kr.city.eng.pendding.dto.TeamAttrDto;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamAttr;
import kr.city.eng.pendding.store.mapper.TbTeamAttrMapper;
import kr.city.eng.pendding.store.repo.TbTeamAttrRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdAttrRepo;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiTeamAttrService {

  private final TbTeamAttrRepo store;
  private final TbTeamAttrMapper mapper;

  private final TbTeamRepo storeTeam;
  private final TbTeamProdAttrRepo storeProdAttr;

  private TbTeamAttr findByIdOrThrow(Long id) {
    return store.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamAttr.class.getName()));
  }

  private TbTeam findTeamOrThrow(Long teamId) {
    return storeTeam.findById(teamId)
        .orElseThrow(() -> ExceptionUtil.id(teamId, TbTeam.class.getName()));
  }

  @Transactional
  public List<TeamAttr> getEntities(Long teamId) {
    TbTeam team = findTeamOrThrow(teamId);
    return store.findByTeam(team).stream()
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public Page<TeamAttr> getEntities(Long teamId, Pageable pageable) {
    TbTeam team = findTeamOrThrow(teamId);
    return store.findByTeam(team, pageable).map(mapper::toDto);
  }

  @Transactional
  public TeamAttr createOrThrow(Long teamId, TeamAttrDto dto) {
    TbTeamAttr entity = mapper.toEntity(dto);
    entity.setTeam(findTeamOrThrow(teamId));
    return mapper.toDto(store.save(entity));
  }

  @Transactional
  public TeamAttr getOrThrow(Long id) {
    return mapper.toDto(findByIdOrThrow(id));
  }

  @Transactional
  public TeamAttr updateOrThrow(Long id, TeamAttrDto team) {
    TbTeamAttr entity = findByIdOrThrow(id);
    mapper.updateEntity(entity, team);
    return mapper.toDto(store.save(entity));
  }

  @Transactional
  public void deleteOrThrow(Long id) {
    store.deleteById(id);
  }

  @Transactional
  public Collection<String> getAttrValues(Long id) {
    TbTeamAttr attr = findByIdOrThrow(id);
    return storeProdAttr.findValuesByAttrGroupBy(attr);
  }

}
