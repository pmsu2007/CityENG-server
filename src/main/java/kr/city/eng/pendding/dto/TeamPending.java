package kr.city.eng.pendding.dto;

import java.util.List;

import org.springframework.util.ObjectUtils;

import kr.city.eng.pendding.store.entity.enums.PendingType;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.Data;
import lombok.experimental.Delegate;

@Data
public class TeamPending implements AppDto {

  private Long id;
  private PendingType type;

  private Long createdAt;
  private String memo;

  private @Delegate List<TeamPendingProd> products;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(type))
      throw ExceptionUtil.require("type");
    if (ObjectUtils.isEmpty(createdAt))
      throw ExceptionUtil.require("createdAt");
    if (ObjectUtils.isEmpty(products))
      throw ExceptionUtil.require("products");
    for (TeamPendingProd data : products) {
      data.validate();
    }
  }

}
