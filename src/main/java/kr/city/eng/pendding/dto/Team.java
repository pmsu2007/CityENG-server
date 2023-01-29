package kr.city.eng.pendding.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Team extends TeamDto {

  private Long id;
  private Long createdAt;
  private Long updatedAt;

}
