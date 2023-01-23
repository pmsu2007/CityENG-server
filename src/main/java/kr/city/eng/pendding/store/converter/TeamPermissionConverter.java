package kr.city.eng.pendding.store.converter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import kr.city.eng.pendding.store.entity.enums.TeamPermission;

@Converter
public class TeamPermissionConverter implements AttributeConverter<Set<TeamPermission>, String> {

  @Override
  public String convertToDatabaseColumn(Set<TeamPermission> attribute) {
    return attribute.stream()
        .map(TeamPermission::getAbbr)
        .collect(Collectors.joining(","));
  }

  @Override
  public Set<TeamPermission> convertToEntityAttribute(String dbData) {
    return Stream.of(dbData.split(","))
        .map(it -> TeamPermission.abbrOf(it.trim()))
        .collect(Collectors.toSet());
  }

}
