package kr.city.eng.pendding.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import kr.city.eng.pendding.dto.Team;
import kr.city.eng.pendding.dto.TeamDto;
import kr.city.eng.pendding.dto.TeamInfo;
import kr.city.eng.pendding.store.entity.team.TbTeam;

@Mapper
public interface TbTeamMapper {

  Team toDto(TbTeam entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "teamProducts", ignore = true)
  @Mapping(target = "teamAttributes", ignore = true)
  @Mapping(target = "teamPlaces", ignore = true)
  @Mapping(target = "teamRoles", ignore = true)
  @Mapping(target = "teamUsers", ignore = true)
  @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())")
  @Mapping(target = "updatedAt", expression = "java(System.currentTimeMillis())")
  TbTeam toEntity(TeamDto dto);

  default void updateEntity(TbTeam entity, TeamDto dto) {
    entity.setName(dto.getName());
    entity.setMemo(dto.getMemo());
    entity.setImageUrl(dto.getImageUrl());
    entity.setUpdatedAt(System.currentTimeMillis());
  }

  default TeamInfo toTeamInfo(TbTeam entity) {
    TeamInfo dto = new TeamInfo();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setImageUrl(entity.getImageUrl());
    dto.setMemo(entity.getMemo());

    dto.setProductCount(entity.getTeamProducts().size());
    dto.setPlaceCount(entity.getTeamPlaces().size());
    dto.setUserCount(entity.getTeamUsers().size());
    return dto;
  }

}
