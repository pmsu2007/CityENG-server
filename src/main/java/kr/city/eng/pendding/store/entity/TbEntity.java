package kr.city.eng.pendding.store.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class TbEntity {

  @Column(nullable = false)
  protected Long createdAt; // timestamp(UTC)
  @Column(nullable = false)
  protected Long updatedAt; // timestamp(UTC)

}
