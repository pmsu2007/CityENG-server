package kr.city.eng.pendding.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TeamProduct extends TeamProductDto {

  private Long id;

  @EqualsAndHashCode.Exclude
  private Long createdAt;
  @EqualsAndHashCode.Exclude
  private Long updatedAt;

}
