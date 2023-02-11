package kr.city.eng.pendding.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.city.eng.pendding.dto.TeamPending;
import kr.city.eng.pendding.dto.TeamPendingProd;
import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.enums.PendingType;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamPending;
import kr.city.eng.pendding.store.entity.team.TbTeamPendingProd;
import kr.city.eng.pendding.store.entity.team.TbTeamPlace;
import kr.city.eng.pendding.store.entity.team.TbTeamProduct;
import kr.city.eng.pendding.store.mapper.TbTeamPendingMapper;
import kr.city.eng.pendding.store.repo.TbTeamPendingProdRepo;
import kr.city.eng.pendding.store.repo.TbTeamPendingRepo;
import kr.city.eng.pendding.store.repo.TbTeamPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamProductRepo;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbUserRepo;
import kr.city.eng.pendding.util.AppUtil;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiTeamPendingService {

  private final TbTeamPendingRepo store;
  private final TbTeamPendingProdRepo storeHist;

  private final TbTeamRepo storeTeam;
  private final TbUserRepo storeUser;
  private final TbTeamPlaceRepo storeTeamPlace;
  private final TbTeamProductRepo storeTeamProduct;

  private final TbTeamPendingMapper mapper;

  private TbTeamPending findByIdOrThrow(Long id) {
    return store.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamPending.class.getName()));
  }

  private TbTeam findTeamOrThrow(Long teamId) {
    return storeTeam.findById(teamId)
        .orElseThrow(() -> ExceptionUtil.id(teamId, TbTeam.class.getName()));
  }

  private TbUser findUserOrThrow(String userId) {
    return storeUser.findById(userId)
        .orElseThrow(() -> ExceptionUtil.id(userId, TbUser.class.getName()));
  }

  private TbTeamPlace findTeamPlaceOrThrow(Long id) {
    return storeTeamPlace.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamPlace.class.getName()));
  }

  private TbTeamProduct findTeamProductOrThrow(Long id) {
    return storeTeamProduct.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamProduct.class.getName()));
  }

  @Transactional
  public List<TeamPending> getEntities(Long teamId, String userId,
      Long startDateTime, Long endDateTime,
      Set<PendingType> types, Set<Long> placeIds) {
    return store.findAllBy(teamId, userId, startDateTime, endDateTime, types, placeIds);
  }

  @Transactional
  public TeamPending getOrThrow(Long id) {
    TbTeamPending entity = findByIdOrThrow(id);
    TeamPending dto = mapper.toDto(entity);
    storeHist.findByPending(entity)
        .forEach(it -> dto.add(mapper.toInfoDto(it)));
    return dto;
  }

  @Transactional
  public TeamPending setPendingType(Long teamId, TeamPending dto) {
    TbTeamPending entity = saveTeamPendingOrThrow(teamId, dto);
    dto.getProducts().forEach(it -> saveTeamPendingProductOrThrow(entity, it));
    return dto;
  }

  private TbTeamPending saveTeamPendingOrThrow(Long teamId, TeamPending dto) {
    TbUser user = findUserOrThrow(AppUtil.getAuthUser());
    TbTeam team = findTeamOrThrow(teamId);

    TbTeamPending entity = mapper.toEntity(dto);
    entity.setUser(user);
    entity.setTeam(team);
    store.save(entity);
    dto.setId(entity.getId());
    return entity;
  }

  private void saveTeamPendingProductOrThrow(TbTeamPending pending, TeamPendingProd hist) {
    TbTeamPendingProd entity = mapper.toInfoEntity(hist);
    entity.setPending(pending);
    entity.setProduct(findTeamProductOrThrow(hist.getProductId()));
    entity.setToPlace(findTeamPlaceOrThrow(hist.getToPlaceId()));
    if (hist.isPlaceEquals()) {
      entity.setFromPlace(entity.getToPlace());
    } else {
      entity.setFromPlace(findTeamPlaceOrThrow(hist.getFromPlaceId()));
    }
    storeHist.save(entity);
    hist.setId(entity.getId());
  }

  @Transactional
  public void deleteOrThrow(Long id) {
    TbTeamPending entity = findByIdOrThrow(id);
    storeHist.deleteByPending(entity);
    store.delete(entity);
  }

}
