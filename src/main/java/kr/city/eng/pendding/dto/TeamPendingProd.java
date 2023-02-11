package kr.city.eng.pendding.dto;

import org.springframework.util.ObjectUtils;

import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.Data;

@Data
public class TeamPendingProd implements AppDto {

  private Long id;
  private Long productId;
  private int quantity;

  private Long toPlaceId;
  private Integer toQuantity;

  private Long fromPlaceId;
  private Integer fromQuantity;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(productId))
      throw ExceptionUtil.require("productId");
    if (ObjectUtils.isEmpty(quantity))
      throw ExceptionUtil.require("quantity");

    if (ObjectUtils.isEmpty(toPlaceId))
      throw ExceptionUtil.require("toPlaceId");
    if (ObjectUtils.isEmpty(toQuantity))
      throw ExceptionUtil.require("toQuantity");

    if (ObjectUtils.isEmpty(fromPlaceId))
      throw ExceptionUtil.require("fromPlaceId");
    if (ObjectUtils.isEmpty(fromQuantity))
      throw ExceptionUtil.require("fromQuantity");
  }

  public boolean isPlaceEquals() {
    return ObjectUtils.nullSafeEquals(toPlaceId, fromPlaceId);
  }

}
