package kr.city.eng.pendding.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.querydsl.core.Tuple;

import kr.city.eng.pendding.dto.TeamPending;
import kr.city.eng.pendding.dto.TeamPendingProd;
import kr.city.eng.pendding.store.entity.team.QTbTeamPending;
import kr.city.eng.pendding.store.entity.team.QTbTeamPendingProd;
import kr.city.eng.pendding.store.entity.team.TbTeamPending;
import kr.city.eng.pendding.store.entity.team.TbTeamPendingProd;

@Mapper
public interface TbTeamPendingMapper {

  public static final QTbTeamPending SCHEMA = QTbTeamPending.tbTeamPending;
  public static final QTbTeamPendingProd PROD = QTbTeamPendingProd.tbTeamPendingProd;

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "team", ignore = true)
  @Mapping(target = "user", ignore = true)
  TbTeamPending toEntity(TeamPending dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pending", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "toPlace", ignore = true)
  @Mapping(target = "fromPlace", ignore = true)
  TbTeamPendingProd toInfoEntity(TeamPendingProd dto);

  @Mapping(target = "products", ignore = true)
  TeamPending toDto(TbTeamPending entity);

  @Mapping(target = "id", expression = "java(tuple.get(SCHEMA.id))")
  @Mapping(target = "type", expression = "java(tuple.get(SCHEMA.type))")
  @Mapping(target = "createdAt", expression = "java(tuple.get(SCHEMA.createdAt))")
  @Mapping(target = "memo", expression = "java(tuple.get(SCHEMA.memo))")
  @Mapping(target = "products", ignore = true)
  TeamPending toDto(Tuple tuple);

  @Mapping(target = "productId", expression = "java(entity.getProduct().getId())")
  @Mapping(target = "toPlaceId", expression = "java(entity.getToPlace().getId())")
  @Mapping(target = "fromPlaceId", expression = "java(entity.getFromPlace().getId())")
  TeamPendingProd toInfoDto(TbTeamPendingProd entity);

  @Mapping(target = "id", expression = "java(tuple.get(PROD.id))")
  @Mapping(target = "quantity", expression = "java(tuple.get(PROD.quantity))")
  @Mapping(target = "productId", expression = "java(tuple.get(PROD.product.id))")
  @Mapping(target = "toPlaceId", expression = "java(tuple.get(PROD.toPlace.id))")
  @Mapping(target = "toQuantity", expression = "java(tuple.get(PROD.toQuantity))")
  @Mapping(target = "fromPlaceId", expression = "java(tuple.get(PROD.fromPlace.id))")
  @Mapping(target = "fromQuantity", expression = "java(tuple.get(PROD.fromQuantity))")
  TeamPendingProd toInfoDto(Tuple tuple);

}
