package kr.city.eng.pendding.store.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;

import kr.city.eng.pendding.dto.TeamAttr;
import kr.city.eng.pendding.dto.TeamAttrDto;
import kr.city.eng.pendding.store.entity.team.TbTeamAttr;

@Mapper
public interface TbTeamAttrMapper {

  @Mapping(target = "values", expression = "java(stringValuesToList(entity.getEnumValues()))")
  TeamAttr toDto(TbTeamAttr entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "team", ignore = true)
  @Mapping(target = "enumValues", expression = "java(listValuesToString(dto.getValues()))")
  @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())")
  @Mapping(target = "updatedAt", expression = "java(System.currentTimeMillis())")
  TbTeamAttr toEntity(TeamAttrDto dto);

  default List<String> stringValuesToList(String value) {
    if (StringUtils.hasText(value)) {
      return Stream.of(value.split(","))
        .map(it -> it.trim())
        .collect(Collectors.toList());
    }
    return Lists.newArrayList();
  }

  default String listValuesToString(List<String> value) {
    if (value!=null) {
      return value.stream().collect(Collectors.joining(","));
    }
    return "";
  }

  default void updateEntity(TbTeamAttr entity, TeamAttrDto dto) {
    entity.setName(dto.getName());
    entity.setType(dto.getType());
    entity.setEnumValues(listValuesToString(dto.getValues()));
    entity.setUpdatedAt(System.currentTimeMillis());
  }

}
