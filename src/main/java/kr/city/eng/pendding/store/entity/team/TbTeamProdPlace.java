package kr.city.eng.pendding.store.entity.team;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode
@Entity
@Table(name = "TB_TEAM_PRODUCT_PLACE")
public class TbTeamProdPlace {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private TbTeamProduct product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id", nullable = false)
  private TbTeamPlace place;

  private int quantity;

  public TbTeamProdPlace(TbTeamProduct product, TbTeamPlace place, int quantity) {
    this.product = product;
    this.place = place;
    this.quantity = quantity;
  }

}
