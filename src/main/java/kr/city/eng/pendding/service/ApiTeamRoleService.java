package kr.city.eng.pendding.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.city.eng.pendding.dto.TeamRole;
import kr.city.eng.pendding.dto.TeamRoleDto;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamRole;
import kr.city.eng.pendding.store.mapper.TbTeamRoleMapper;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbTeamRoleRepo;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiTeamRoleService {

  private final TbTeamRoleRepo store;
  private final TbTeamRoleMapper mapper;

  private final TbTeamRepo storeTeam;

  private TbTeamRole findByIdOrThrow(Long id) {
    return store.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamRole.class.getName()));
  }

  private TbTeam findTeamOrThrow(Long teamId) {
    return storeTeam.findById(teamId)
        .orElseThrow(() -> ExceptionUtil.id(teamId, TbTeam.class.getName()));
  }

  @Transactional
  public List<TeamRole> getEntities(Long teamId) {
    TbTeam team = findTeamOrThrow(teamId);
    return store.findByTeam(team).stream()
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public TeamRole createOrThrow(Long teamId, TeamRoleDto dto) {
    TbTeamRole entity = mapper.toEntity(dto);
    entity.setTeam(findTeamOrThrow(teamId));
    return mapper.toDto(store.save(entity));
  }

  @Transactional
  public TeamRole getOrThrow(Long id) {
    return mapper.toDto(findByIdOrThrow(id));
  }

  @Transactional
  public TeamRole updateOrThrow(Long id, TeamRoleDto team) {
    TbTeamRole entity = findByIdOrThrow(id);
    mapper.updateEntity(entity, team);
    return mapper.toDto(store.save(entity));
  }

  @Transactional
  public void deleteOrThrow(Long id) {
    TbTeamRole entity = findByIdOrThrow(id);
    if (entity.isSystem()) {
      ExceptionUtil.system(id);
    }
    store.deleteById(id);
  }

}
