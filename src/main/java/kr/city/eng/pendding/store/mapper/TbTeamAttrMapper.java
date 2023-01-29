package kr.city.eng.pendding.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import kr.city.eng.pendding.dto.TeamAttr;
import kr.city.eng.pendding.dto.TeamAttrDto;
import kr.city.eng.pendding.store.entity.team.TbTeamAttr;

@Mapper
public interface TbTeamAttrMapper {

  TeamAttr toDto(TbTeamAttr entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "team", ignore = true)
  @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())")
  @Mapping(target = "updatedAt", expression = "java(System.currentTimeMillis())")
  TbTeamAttr toEntity(TeamAttrDto dto);

  default void updateEntity(TbTeamAttr entity, TeamAttrDto dto) {
    entity.setName(dto.getName());
    entity.setType(dto.getType());
    entity.setUpdatedAt(System.currentTimeMillis());
  }

}
