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
import kr.city.eng.pendding.store.entity.team.TbTeamProdPlace;
import kr.city.eng.pendding.store.entity.team.TbTeamProduct;
import kr.city.eng.pendding.store.mapper.TbTeamPendingMapper;
import kr.city.eng.pendding.store.repo.TbTeamPendingProdRepo;
import kr.city.eng.pendding.store.repo.TbTeamPendingRepo;
import kr.city.eng.pendding.store.repo.TbTeamPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdPlaceRepo;
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
  private final TbTeamProdPlaceRepo storeProdPlace;

  private final TbTeamRepo storeTeam;
  private final TbUserRepo storeUser;
  private final TbTeamPlaceRepo storeTeamPlace;
  private final TbTeamProductRepo storeTeamProduct;

  private final TbTeamPendingMapper mapper;
  private final ApiTeamPermission teamPermission;

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
    dto.forEach(it -> saveTeamPendingProductOrThrow(entity, it));
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
    TbTeamPlace toPlace = findTeamPlaceOrThrow(hist.getToPlaceId());
    TbTeamPlace fromPlace = toPlace;
    if (!hist.isPlaceEquals()) {
      fromPlace = findTeamPlaceOrThrow(hist.getFromPlaceId());
    }
    entity.setProduct(findTeamProductOrThrow(hist.getProductId()));
    entity.setToPlace(toPlace);
    entity.setFromPlace(fromPlace);

    saveTeamProdcutPlace(entity, pending.getType(), hist);
    storeHist.save(entity);
    hist.setId(entity.getId());
  }

  private void saveTeamProdcutPlace(TbTeamPendingProd pending,
      PendingType type, TeamPendingProd hist) {
    TbTeamProduct product = pending.getProduct();
    TbTeamPlace toPlace = pending.getToPlace();
    if (type.equals(PendingType.MOVE)) {
      TbTeamPlace fromPlace = pending.getFromPlace();
      saveTeamProdcutPlaceMove(product, fromPlace, toPlace, hist);
    } else {
      saveTeamProdcutPlace(product, toPlace, hist, type);
    }
  }

  private TbTeamProdPlace getTbTeamProdPlace(TbTeamProduct product, TbTeamPlace place) {
    return storeProdPlace
        .findByProductIdAndPlaceId(product.getId(), place.getId())
        .orElseGet(() -> {
          TbTeamProdPlace newPlace = new TbTeamProdPlace();
          newPlace.setProduct(product);
          newPlace.setPlace(place);
          return newPlace;
        });
  }

  private void saveTeamProdcutPlace(TbTeamProduct product, TbTeamPlace place, TeamPendingProd hist, PendingType type) {
    TbTeamProdPlace entity = getTbTeamProdPlace(product, place);
    int quantity = entity.getQuantity();
    hist.setFromQuantity(quantity);
    hist.setToQuantity(quantity);
    entity.setQuantity(hist.adjustToQuantity(type, quantity));
    storeProdPlace.save(entity);
  }

  private void saveTeamProdcutPlaceMove(TbTeamProduct product, TbTeamPlace fromPlace, TbTeamPlace toPlace,
      TeamPendingProd hist) {
    TbTeamProdPlace entity = getTbTeamProdPlace(product, fromPlace);
    int quantity = entity.getQuantity();
    hist.setFromQuantity(quantity);
    entity.setQuantity(quantity - hist.getQuantity());
    storeProdPlace.save(entity);

    entity = getTbTeamProdPlace(product, toPlace);
    quantity = entity.getQuantity();
    hist.setToQuantity(quantity);
    entity.setQuantity(quantity + hist.getQuantity());
    storeProdPlace.save(entity);
  }

  @Transactional
  public void deleteOrThrow(Long teamId, Long id) {
    TbTeamPending entity = findByIdOrThrow(id);
    teamPermission.verify(teamId,
        entity.getType().toTeamPermission());

    storeHist.deleteByPending(entity);
    store.delete(entity);
  }

}
