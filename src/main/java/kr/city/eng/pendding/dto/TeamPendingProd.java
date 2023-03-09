package kr.city.eng.pendding.dto;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.city.eng.pendding.store.entity.enums.PendingType;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.Data;

@Data
public class TeamPendingProd implements AppDto {

  private Long id;
  private Long productId;
  private int quantity;

  private Long toPlaceId;
  private Long fromPlaceId;

  private Integer toQuantity;
  private Integer fromQuantity;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(productId))
      throw ExceptionUtil.require("productId");
    if (ObjectUtils.isEmpty(quantity))
      throw ExceptionUtil.require("quantity");

    if (ObjectUtils.isEmpty(toPlaceId))
      throw ExceptionUtil.require("toPlaceId");

    if (ObjectUtils.isEmpty(fromPlaceId))
      throw ExceptionUtil.require("fromPlaceId");
  }

  @JsonIgnore
  public boolean isPlaceEquals() {
    return ObjectUtils.nullSafeEquals(toPlaceId, fromPlaceId);
  }

  public int adjustToQuantity(PendingType type, int fromQuantity) {
    switch (type) {
      case IN:
        return fromQuantity + quantity;
      case OUT:
        return fromQuantity - quantity;
      case ADJUST:
        return quantity;
      default:
        break;
    }
    return fromQuantity;
  }

}
