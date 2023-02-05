package kr.city.eng.pendding.store.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "TB_SYSTEM")
public class TbSystem implements TbEntity {

  @Id
  @EqualsAndHashCode.Include
  private String key;

  private String value;

}
