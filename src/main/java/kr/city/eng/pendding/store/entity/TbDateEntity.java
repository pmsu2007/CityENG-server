package kr.city.eng.pendding.store.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class TbDateEntity implements TbEntity {

  @Column(nullable = false)
  protected long createdAt; // timestamp(UTC)
  @Column(nullable = false)
  protected long updatedAt; // timestamp(UTC)

}
