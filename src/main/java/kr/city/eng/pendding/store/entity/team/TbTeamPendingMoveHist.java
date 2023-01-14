package kr.city.eng.pendding.store.entity.team;

import javax.persistence.Column;
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

@Data
@Entity
@Table(name = "TB_TEAM_PENDING_MOVE_HIST")
public class TbTeamPendingMoveHist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "pending_id", nullable = false)
  private TbTeamPending pending;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private TbTeamProduct product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "from_place_id", nullable = false)
  private TbTeamPlace fromPlace;
  @Column(nullable = false)
  private Integer fromQuantity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "to_place_id", nullable = false)
  private TbTeamPlace toPlace;
  @Column(nullable = false)
  private Integer toQuantity;

  private int quantity;

}
