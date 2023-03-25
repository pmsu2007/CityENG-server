package kr.city.eng.pendding.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TeamAttr extends TeamAttrDto {

  private Long id;

  @EqualsAndHashCode.Exclude
  private Long createdAt;
  @EqualsAndHashCode.Exclude
  private Long updatedAt;

}
