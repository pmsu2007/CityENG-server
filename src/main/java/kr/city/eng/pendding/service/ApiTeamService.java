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
import kr.city.eng.pendding.store.mapper.TbTeamMapper;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbUserRepo;
import kr.city.eng.pendding.util.AppUtil;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiTeamService {

  private final TbUserRepo storeUser;

  private final TbTeamRepo storeTeam;
  private final TbTeamMapper teamMapper;

  private TbTeam findByIdOrThrow(Long id) {
    return storeTeam.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeam.class.getName()));
  }

  private TbUser findUserOrThrow(String userId) {
    return storeUser.findById(userId)
        .orElseThrow(() -> ExceptionUtil.id(userId, TbUser.class.getName()));
  }

  @Transactional
  public List<Team> getTeams() {
    return storeTeam.findAll().stream()
        .map(teamMapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public Page<Team> getTeamsWithPage(Pageable pageable) {
    return storeTeam.findAll(pageable).map(teamMapper::toDto);
  }

  @Transactional
  public Team getTeamOrThrow(Long id) {
    return teamMapper.toDto(findByIdOrThrow(id));
  }

  @Transactional
  public Team createOrThrow(TeamDto dto) {
    TbTeam entity = teamMapper.toEntity(dto);
    entity.setUser(findUserOrThrow(AppUtil.getAuthUser()));
    return teamMapper.toDto(storeTeam.save(entity));
  }

  @Transactional
  public Team updateOrThrow(Long id, TeamDto team) {
    TbTeam entity = findByIdOrThrow(id);
    teamMapper.updateEntity(entity, team);
    return teamMapper.toDto(storeTeam.save(entity));
  }

  @Transactional
  public void deleteOrThrow(Long id) {
    storeTeam.deleteById(id);
  }

}
