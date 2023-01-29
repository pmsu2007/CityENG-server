package kr.city.eng.pendding.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import kr.city.eng.pendding.dto.TeamRole;
import kr.city.eng.pendding.dto.TeamRoleDto;
import kr.city.eng.pendding.store.entity.team.TbTeamRole;

@Mapper
public interface TbTeamRoleMapper {

  TeamRole toDto(TbTeamRole entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "system", ignore = true)
  @Mapping(target = "team", ignore = true)
  @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())")
  @Mapping(target = "updatedAt", expression = "java(System.currentTimeMillis())")
  TbTeamRole toEntity(TeamRoleDto dto);

  default void updateEntity(TbTeamRole entity, TeamRoleDto dto) {
    entity.setName(dto.getName());
    entity.setPermissions(dto.getPermissions());
    entity.setUpdatedAt(System.currentTimeMillis());
  }

}
