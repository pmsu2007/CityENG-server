package kr.city.eng.pendding.store.entity.team;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kr.city.eng.pendding.store.entity.TbDateEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_TEAM_PRODUCT", indexes = {
    @Index(name = "IDX_TEAM_PRODUCT_NAME", columnList = "name"),
    @Index(name = "IDX_TEAM_PRODUCT_BARCODE", columnList = "barcode", unique = true)
})
public class TbTeamProduct extends TbDateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(nullable = false)
  private String name;
  @Column(nullable = false, unique = true)
  private String barcode;

  private String imageUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id", nullable = false)
  private TbTeam team;

}
