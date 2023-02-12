package kr.city.eng.pendding.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@Entity
@Table(name = "TB_SYSTEM")
public class TbSystem implements TbEntity {

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Getter
  public enum Prop {
    VER("version");

    private String property;
  }

  @Id
  @EqualsAndHashCode.Include
  private String propKey;

  private String propVal;

}
