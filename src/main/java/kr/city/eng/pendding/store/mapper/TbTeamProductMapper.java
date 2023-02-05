package kr.city.eng.pendding.store.mapper;

import static kr.city.eng.pendding.store.entity.team.QTbTeamProdAttr.tbTeamProdAttr;
import static kr.city.eng.pendding.store.entity.team.QTbTeamProdPlace.tbTeamProdPlace;
import static kr.city.eng.pendding.store.entity.team.QTbTeamProduct.tbTeamProduct;

import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.ObjectUtils;

import com.querydsl.core.Tuple;

import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.dto.TeamProductDto;
import kr.city.eng.pendding.store.entity.team.TbTeamProduct;

@Mapper
public interface TbTeamProductMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "team", ignore = true)
  @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())")
  @Mapping(target = "updatedAt", expression = "java(System.currentTimeMillis())")
  TbTeamProduct toEntity(TeamProductDto dto);

  default boolean patchEntity(TbTeamProduct entity, TeamProductDto dto) {
    boolean result = false;
    if (!ObjectUtils.isEmpty(dto.getName())) {
      entity.setName(dto.getName());
      result = true;
    }
    if (!ObjectUtils.isEmpty(dto.getImageUrl())) {
      entity.setImageUrl(dto.getImageUrl());
      result = true;
    }

    if (result) {
      entity.setUpdatedAt(System.currentTimeMillis());
    }
    return result;
  }

  @Mapping(target = "places", ignore = true)
  @Mapping(target = "attributes", ignore = true)
  TeamProduct toDto(TbTeamProduct entity);

  default TeamProduct tupleToTeamProduct(Tuple tuple) {
    TeamProduct dto = new TeamProduct();
    dto.setId(tuple.get(tbTeamProduct.id));
    dto.setBarcode(tuple.get(tbTeamProduct.barcode));
    dto.setName(tuple.get(tbTeamProduct.name));
    dto.setImageUrl(tuple.get(tbTeamProduct.imageUrl));
    dto.setCreatedAt(tuple.get(tbTeamProduct.createdAt));
    dto.setUpdatedAt(tuple.get(tbTeamProduct.updatedAt));
    return dto;
  }

  default void tupleToTeamProdAttr(Tuple tuple, Map<Long, TeamProduct> map) {
    Long id = tuple.get(tbTeamProdAttr.product.id);
    TeamProduct dto;
    if ((dto = map.get(id)) != null) {
      tupleToTeamProdAttr(tuple, dto);
    }
  }

  default void tupleToTeamProdAttr(Tuple tuple, TeamProduct dto) {
    dto.addAttribute(tuple.get(tbTeamProdAttr.id), tuple.get(tbTeamProdAttr.attrValue));
  }

  default void tupleToTeamProdPlace(Tuple tuple, Map<Long, TeamProduct> map) {
    Long id = tuple.get(tbTeamProdPlace.product.id);
    TeamProduct dto;
    if ((dto = map.get(id)) != null) {
      tupleToTeamProdPlace(tuple, dto);
    }
  }

  default void tupleToTeamProdPlace(Tuple tuple, TeamProduct dto) {
    dto.addPlace(tuple.get(tbTeamProdPlace.id), tuple.get(tbTeamProdPlace.quantity));
  }

}
