package kr.city.eng.pendding.dto;

import org.springframework.util.ObjectUtils;

import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.Data;

@Data
public class TeamDto implements AppDto {

  protected String name;
  protected String imageUrl;
  protected String memo;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(name))
      throw ExceptionUtil.require("name");
  }

}
