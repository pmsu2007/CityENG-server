package kr.city.eng.pendding.dto;

import org.springframework.util.ObjectUtils;

import kr.city.eng.pendding.store.entity.enums.AuthType;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.Data;

@Data
public class UserDto implements AppDto {

  private String id;
  private String password;

  private AuthType authentication;
  private String name;
  private String email;
  private String imageUrl;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(id))
      throw ExceptionUtil.require("id");
    if (ObjectUtils.isEmpty(password))
      throw ExceptionUtil.require("password");
    if (ObjectUtils.isEmpty(authentication))
      throw ExceptionUtil.require("authentication");
    if (ObjectUtils.isEmpty(name))
      throw ExceptionUtil.require("name");
  }

  public void validateUpdate() {
    if (ObjectUtils.isEmpty(authentication))
      throw ExceptionUtil.require("authentication");
    if (ObjectUtils.isEmpty(name))
      throw ExceptionUtil.require("name");
    if (ObjectUtils.isEmpty(email))
      throw ExceptionUtil.require("email");
  }

}
