package kr.city.eng.pendding.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.city.eng.pendding.dto.Team;
import kr.city.eng.pendding.dto.TeamDto;
import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamRole;
import kr.city.eng.pendding.store.mapper.TbTeamMapper;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbTeamRoleRepo;
import kr.city.eng.pendding.store.repo.TbUserRepo;
import kr.city.eng.pendding.util.AppUtil;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiTeamService {

  private final TbTeamRepo store;
  private final TbTeamMapper mapper;

  private final TbTeamRoleRepo storeRole;
  private final TbUserRepo storeUser;

  private TbTeam findByIdOrThrow(Long id) {
    return store.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeam.class.getName()));
  }

  private TbUser findUserOrThrow(String userId) {
    return storeUser.findById(userId)
        .orElseThrow(() -> ExceptionUtil.id(userId, TbUser.class.getName()));
  }

  @Transactional
  public List<Team> getEntities() {
    return store.findAll().stream()
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public Page<Team> getTeamsWithPage(Pageable pageable) {
    return store.findAll(pageable).map(mapper::toDto);
  }

  @Transactional
  public Team getOrThrow(Long id) {
    return mapper.toDto(findByIdOrThrow(id));
  }

  @Transactional
  public Team createOrThrow(TeamDto dto) {
    TbTeam entity = mapper.toEntity(dto);
    entity.setUser(findUserOrThrow(AppUtil.getAuthUser()));
    entity = store.save(entity);
    createRoleOrThrow(entity);
    return mapper.toDto(entity);
  }

  private void createRoleOrThrow(TbTeam team) {
    TbTeamRole entity = TbTeamRole.admin();
    entity.setTeam(team);
    storeRole.save(entity);

    entity = TbTeamRole.member();
    entity.setTeam(team);
    storeRole.save(entity);
  }

  @Transactional
  public Team updateOrThrow(Long id, TeamDto dto) {
    TbTeam entity = findByIdOrThrow(id);
    mapper.updateEntity(entity, dto);
    return mapper.toDto(store.save(entity));
  }

  @Transactional
  public void deleteOrThrow(Long id) {
    store.deleteById(id);
  }

}
