package kr.city.eng.pendding.dto;

import org.springframework.util.ObjectUtils;

import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamUser implements AppDto {

  private String userId;
  private Long teamRoleId;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(userId))
      throw ExceptionUtil.require("userId");
    if (ObjectUtils.isEmpty(teamRoleId))
      throw ExceptionUtil.require("teamRoleId");
  }

}
