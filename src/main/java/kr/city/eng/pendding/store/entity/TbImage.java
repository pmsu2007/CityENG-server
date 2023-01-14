package kr.city.eng.pendding.store.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TB_IMAGE")
public class TbImage {

  @Id
  private String id; // md5+id
  private String name;
  private Long size;

}
