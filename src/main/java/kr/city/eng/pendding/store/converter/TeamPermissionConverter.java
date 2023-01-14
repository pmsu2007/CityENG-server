package kr.city.eng.pendding.store.converter;

import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import kr.city.eng.pendding.store.entity.enums.TeamPermission;

@Converter
public class TeamPermissionConverter implements AttributeConverter<Set<TeamPermission>, String> {

  @Override
  public String convertToDatabaseColumn(Set<TeamPermission> attribute) {
    return null;
  }

  @Override
  public Set<TeamPermission> convertToEntityAttribute(String dbData) {
    return null;
  }

}
