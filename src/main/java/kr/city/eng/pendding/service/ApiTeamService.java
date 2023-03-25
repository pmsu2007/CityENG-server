package kr.city.eng.pendding.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.city.eng.pendding.dto.Team;
import kr.city.eng.pendding.dto.TeamDto;
import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamRole;
import kr.city.eng.pendding.store.entity.team.TbTeamUser;
import kr.city.eng.pendding.store.mapper.TbTeamMapper;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbTeamRoleRepo;
import kr.city.eng.pendding.store.repo.TbTeamUserRepo;
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
  private final TbTeamUserRepo storeTeamUser;

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
    TbUser user = findUserOrThrow(AppUtil.getAuthUser());
    return storeTeamUser.findByUser(user).stream().map(it -> mapper.toDto(it.getTeam()))
        .collect(Collectors.toList());
  }

  @Transactional
  public Team getOrThrow(Long id) {
    return mapper.toDto(findByIdOrThrow(id));
  }

  @Transactional
  public Team createOrThrow(TeamDto dto) {
    TbTeam entity = mapper.toEntity(dto);
    TbUser user = findUserOrThrow(AppUtil.getAuthUser());
    entity.setUser(user);
    entity = store.save(entity);
    TbTeamRole role = createTeamRoleOrThrow(entity);
    createTeamUserOrThrow(entity, role, user);
    return mapper.toDto(entity);
  }

  private TbTeamRole createTeamRoleOrThrow(TbTeam team) {
    TbTeamRole entity = TbTeamRole.member();
    entity.setTeam(team);
    storeRole.save(entity);

    entity = TbTeamRole.admin();
    entity.setTeam(team);
    storeRole.save(entity);
    return entity;
  }

  private TbTeamUser createTeamUserOrThrow(TbTeam team, TbTeamRole role, TbUser user) {
    TbTeamUser entity = new TbTeamUser();
    entity.setTeam(team);
    entity.setTeamRole(role);
    entity.setUser(user);
    return storeTeamUser.save(entity);
  }

  @Transactional
  public Team updateOrThrow(Long id, TeamDto dto) {
    TbTeam entity = findByIdOrThrow(id);
    mapper.updateEntity(entity, dto);
    return mapper.toDto(store.save(entity));
  }

  @Transactional
  public void deleteOrThrow(Long id) {
    TbTeam entity = findByIdOrThrow(id);
    deleteOrThrow(entity);
  }

  @Transactional
  public void deleteOrThrow(TbTeam entity) {
    storeTeamUser.deleteByTeam(entity);
    storeTeamUser.flush();
    store.delete(entity);
  }

}
