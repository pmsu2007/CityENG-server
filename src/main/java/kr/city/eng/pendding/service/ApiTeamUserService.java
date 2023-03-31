package kr.city.eng.pendding.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.city.eng.pendding.dto.TeamUser;
import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamRole;
import kr.city.eng.pendding.store.entity.team.TbTeamUser;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbTeamRoleRepo;
import kr.city.eng.pendding.store.repo.TbTeamUserRepo;
import kr.city.eng.pendding.store.repo.TbUserRepo;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiTeamUserService {

  private final TbTeamUserRepo store;

  private final TbTeamRepo storeTeam;
  private final TbTeamRoleRepo storeRole;
  private final TbUserRepo storeUser;

  private final ApiTeamPermission teamPermission;

  private TbTeam findTeamOrThrow(Long id) {
    return storeTeam.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeam.class.getName()));
  }

  private TbUser findUserOrThrow(String userId) {
    return storeUser.findById(userId)
        .orElseThrow(() -> ExceptionUtil.id(userId, TbUser.class.getName()));
  }

  private TbTeamRole findRoleOrThrow(Long id) {
    return storeRole.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamRole.class.getName()));
  }

  @Transactional
  public TeamUser createTeamUserOrThrow(Long teamId, TeamUser dto) {
    TbTeam team = findTeamOrThrow(teamId);
    TbUser user = findUserOrThrow(dto.getUserId());
    TbTeamRole role = findRoleOrThrow(dto.getTeamRoleId());
    createTeamUserOrThrow(team, role, user);
    teamPermission.add(teamId, dto.getUserId(), role.getPermissions());
    return dto;
  }

  @Transactional
  public TeamUser updateTeamUserOrThrow(Long teamId, TeamUser dto) {
    TbTeamRole role = findRoleOrThrow(dto.getTeamRoleId());
    TbTeamUser entity = store.findByTeamAndUser(teamId, dto.getUserId())
        .orElseThrow(() -> {
          String msg = "teamId: " + teamId + ", userId: " + dto.getUserId();
          return ExceptionUtil.id(msg, TbTeamUser.class.getName());
        });
    entity.setTeamRole(role);
    entity.setUpdatedAt(System.currentTimeMillis());
    store.save(entity);
    teamPermission.add(teamId, dto.getUserId(), role.getPermissions());
    return dto;
  }

  private TbTeamUser createTeamUserOrThrow(TbTeam team, TbTeamRole role, TbUser user) {
    TbTeamUser entity = new TbTeamUser();
    entity.setTeam(team);
    entity.setTeamRole(role);
    entity.setUser(user);
    entity.setCreatedAt(System.currentTimeMillis());
    entity.setUpdatedAt(System.currentTimeMillis());
    return store.save(entity);
  }

  @Transactional
  public void deleteTeamUserOrThrow(Long teamId, String userId) {
    TbTeamUser entity = store
        .findByTeamAndUser(teamId, userId)
        .orElseThrow();
    store.delete(entity);
  }

  @Transactional
  public List<TeamUser> getTeamUsers(Long teamId) {
    TbTeam team = findTeamOrThrow(teamId);
    return store.findByTeam(team).stream()
        .map(it -> new TeamUser(it.getUser().getId(),
            it.getTeamRole().getId()))
        .toList();
  }

}
