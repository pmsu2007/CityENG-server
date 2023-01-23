package kr.city.eng.pendding.dto;

import org.springframework.util.ObjectUtils;

import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.Data;

@Data
public class TeamPlaceDto implements AppDto {

  private String name;
  private String memo;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(name))
      throw ExceptionUtil.require("name");
  }

}
