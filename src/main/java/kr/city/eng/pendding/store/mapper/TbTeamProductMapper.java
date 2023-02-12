package kr.city.eng.pendding.store.mapper;

import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.ObjectUtils;

import com.querydsl.core.Tuple;

import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.dto.TeamProductDto;
import kr.city.eng.pendding.store.StoreConfig;
import kr.city.eng.pendding.store.entity.team.TbTeamProduct;

@Mapper(imports = { StoreConfig.class })
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

  @Mapping(target = "id", expression = "java(tuple.get(StoreConfig.PRODUCT.id))")
  @Mapping(target = "barcode", expression = "java(tuple.get(StoreConfig.PRODUCT.barcode))")
  @Mapping(target = "name", expression = "java(tuple.get(StoreConfig.PRODUCT.name))")
  @Mapping(target = "imageUrl", expression = "java(tuple.get(StoreConfig.PRODUCT.imageUrl))")
  @Mapping(target = "createdAt", expression = "java(tuple.get(StoreConfig.PRODUCT.createdAt))")
  @Mapping(target = "updatedAt", expression = "java(tuple.get(StoreConfig.PRODUCT.updatedAt))")
  @Mapping(target = "places", ignore = true)
  @Mapping(target = "attributes", ignore = true)
  TeamProduct toDto(Tuple tuple);

  default void setTeamProdAttr(Tuple tuple, Map<Long, TeamProduct> map) {
    Long id = tuple.get(StoreConfig.PROD_ATTR.product.id);
    TeamProduct dto;
    if ((dto = map.get(id)) != null) {
      setTeamProdAttr(tuple, dto);
    }
  }

  default void setTeamProdAttr(Tuple tuple, TeamProduct dto) {
    dto.addAttribute(tuple.get(StoreConfig.PROD_ATTR.id), tuple.get(StoreConfig.PROD_ATTR.attrValue));
  }

  default void setTeamProdPlace(Tuple tuple, Map<Long, TeamProduct> map) {
    Long id = tuple.get(StoreConfig.PROD_PLACE.product.id);
    TeamProduct dto;
    if ((dto = map.get(id)) != null) {
      setTeamProdPlace(tuple, dto);
    }
  }

  default void setTeamProdPlace(Tuple tuple, TeamProduct dto) {
    dto.addPlace(tuple.get(StoreConfig.PROD_PLACE.id), tuple.get(StoreConfig.PROD_PLACE.quantity));
  }

}
