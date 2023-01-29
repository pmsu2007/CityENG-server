package kr.city.eng.pendding.dto;

import java.util.Set;

import org.springframework.util.ObjectUtils;

import kr.city.eng.pendding.store.entity.enums.TeamPermission;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.Data;

@Data
public class TeamRoleDto implements AppDto {

  protected String name;
  protected Set<TeamPermission> permissions;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(permissions))
      throw ExceptionUtil.require("permissions");
  }

}
