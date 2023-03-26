package kr.city.eng.pendding.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.city.eng.pendding.dto.TeamPending;
import kr.city.eng.pendding.dto.TeamPendingProd;
import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.dto.TeamProductDto;
import kr.city.eng.pendding.dto.TeamProductDto.Attr;
import kr.city.eng.pendding.dto.TeamProductDto.Place;
import kr.city.eng.pendding.store.entity.enums.PendingType;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamAttr;
import kr.city.eng.pendding.store.entity.team.TbTeamPlace;
import kr.city.eng.pendding.store.entity.team.TbTeamProdAttr;
import kr.city.eng.pendding.store.entity.team.TbTeamProdPlace;
import kr.city.eng.pendding.store.entity.team.TbTeamProduct;
import kr.city.eng.pendding.store.mapper.TbTeamProductMapper;
import kr.city.eng.pendding.store.repo.TbTeamAttrRepo;
import kr.city.eng.pendding.store.repo.TbTeamPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdAttrRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamProductRepo;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiTeamProductService {

  private final TbTeamProductRepo store;
  private final TbTeamProdPlaceRepo storeProdPlace;
  private final TbTeamProdAttrRepo storeProdAttr;
  private final TbTeamProductMapper mapper;

  private final TbTeamRepo storeTeam;
  private final TbTeamPlaceRepo storeTeamPlace;
  private final TbTeamAttrRepo storeTeamAttr;

  private final ApiTeamPendingService pendingService;

  private TbTeamProduct findByIdOrThrow(Long id) {
    return store.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamProduct.class.getName()));
  }

  private TbTeam findTeamOrThrow(Long teamId) {
    return storeTeam.findById(teamId)
        .orElseThrow(() -> ExceptionUtil.id(teamId, TbTeam.class.getName()));
  }

  private TbTeamPlace findTeamPlaceOrThrow(Long id) {
    return storeTeamPlace.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamPlace.class.getName()));
  }

  private TbTeamAttr findTemaAttrOrThrow(Long id) {
    return storeTeamAttr.findById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamAttr.class.getName()));
  }

  @Transactional
  public Page<TeamProduct> getEntities(Long teamId, String value, Pageable pageable) {
    TbTeam team = findTeamOrThrow(teamId);
    return store.findDtoByTeam(team, value, pageable);
  }

  @Transactional
  public TeamProduct createOrThrow(Long teamId, TeamProductDto dto) {
    TbTeamProduct entity = mapper.toEntity(dto);
    entity.setTeam(findTeamOrThrow(teamId));
    entity = store.save(entity);

    for (Attr attrDto : dto.getAttributes()) {
      TbTeamAttr attr = findTemaAttrOrThrow(attrDto.getId());
      storeProdAttr.save(new TbTeamProdAttr(entity, attr, attrDto.getValue()));
    }

    for (Place placeDto : dto.getPlaces()) {
      TbTeamPlace place = findTeamPlaceOrThrow(placeDto.getId());
      storeProdPlace.save(new TbTeamProdPlace(entity, place, 0));
      // 히스토리의 조정으로 추가해야함.
      createAdjustPending(teamId, entity, placeDto);
    }

    TeamProduct result = mapper.toDto(entity);
    result.setPlaces(dto.getPlaces());
    result.setAttributes(dto.getAttributes());

    return result;
  }

  private void createAdjustPending(Long teamId, TbTeamProduct product, Place place) {
    TeamPending pending = new TeamPending();
    pending.setType(PendingType.ADJUST);
    pending.setCreatedAt(product.getCreatedAt());

    TeamPendingProd prodDto = new TeamPendingProd();
    prodDto.setQuantity(place.getQuantity());
    prodDto.setProductId(product.getId());
    prodDto.setToPlaceId(place.getId());
    prodDto.setFromPlaceId(place.getId());

    pending.add(prodDto);
    pendingService.setPendingType(teamId, pending);
  }

  @Transactional
  public TeamProduct getOrThrow(Long id) {
    return store.findDtoById(id)
        .orElseThrow(() -> ExceptionUtil.id(id, TbTeamProduct.class.getName()));
  }

  public Place getTeamProductPlaceOrThrow(Long productId, Long placeId) {
    return store.findProductPlaceBy(productId, placeId).orElseThrow();
  }

  @Transactional
  public TeamProduct patchOrThrow(Long id, TeamProductDto team) {
    TbTeamProduct entity = findByIdOrThrow(id);
    if (mapper.patchEntity(entity, team)) {
      store.save(entity);
    }

    team.getAttributes().forEach(it -> storeProdAttr.findById(id).ifPresent(attr -> {
      attr.setAttrValue(it.getValue());
      storeProdAttr.save(attr);
    }));
    return getOrThrow(id);
  }

  @Transactional
  public void deleteOrThrow(Long id) {
    storeProdPlace.deleteByProductId(id);
    storeProdAttr.deleteByProductId(id);
    store.deleteById(id);
  }

}
