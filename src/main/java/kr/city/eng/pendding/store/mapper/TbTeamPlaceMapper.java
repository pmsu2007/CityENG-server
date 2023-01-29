package kr.city.eng.pendding.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import kr.city.eng.pendding.dto.TeamPlace;
import kr.city.eng.pendding.dto.TeamPlaceDto;
import kr.city.eng.pendding.store.entity.team.TbTeamPlace;

@Mapper
public interface TbTeamPlaceMapper {

  TeamPlace toDto(TbTeamPlace entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "team", ignore = true)
  @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())")
  @Mapping(target = "updatedAt", expression = "java(System.currentTimeMillis())")
  TbTeamPlace toEntity(TeamPlaceDto dto);

  default void updateEntity(TbTeamPlace entity, TeamPlaceDto dto) {
    entity.setName(dto.getName());
    entity.setMemo(dto.getMemo());
    entity.setUpdatedAt(System.currentTimeMillis());
  }

}
