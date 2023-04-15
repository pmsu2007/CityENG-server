package kr.city.eng.pendding.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import kr.city.eng.pendding.dto.User;
import kr.city.eng.pendding.dto.UserDto;
import kr.city.eng.pendding.store.entity.TbUser;

@Mapper
public interface TbUserMapper {

  User toDto(TbUser entity);

  @Mapping(target = "system", ignore = true)
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "teams", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "apikey", ignore = true)
  @Mapping(target = "createdAt", expression = "java(System.currentTimeMillis())")
  @Mapping(target = "updatedAt", expression = "java(System.currentTimeMillis())")
  TbUser toEntity(UserDto entity);

  default void updateEntity(TbUser entity, UserDto dto) {
    entity.setAuthentication(dto.getAuthentication());
    entity.setName(dto.getName());
    entity.setEmail(dto.getEmail());
    entity.setImageUrl(dto.getImageUrl());
    entity.setUpdatedAt(System.currentTimeMillis());
  }

}
