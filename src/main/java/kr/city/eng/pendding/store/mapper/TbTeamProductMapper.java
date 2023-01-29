package kr.city.eng.pendding.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.dto.TeamProductDto;
import kr.city.eng.pendding.store.entity.team.TbTeamProduct;

@Mapper
public interface TbTeamProductMapper {

  TeamProduct toDto(TbTeamProduct entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "team", ignore = true)
  @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())")
  @Mapping(target = "updatedAt", expression = "java(System.currentTimeMillis())")
  TbTeamProduct toEntity(TeamProductDto dto);

  default void updateName(TbTeamProduct entity, String name) {
    entity.setName(name);
    entity.setUpdatedAt(System.currentTimeMillis());
  }

  default void updateImageUrl(TbTeamProduct entity, String imageUrl) {
    entity.setImageUrl(imageUrl);
    entity.setUpdatedAt(System.currentTimeMillis());
  }

}
