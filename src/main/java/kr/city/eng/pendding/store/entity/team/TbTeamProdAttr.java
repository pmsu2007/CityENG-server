package kr.city.eng.pendding.store.entity.team;

import javax.persistence.AssociationOverride;
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
@EqualsAndHashCode
@Entity
@Table(name = "TB_TEAM_PRODUCT_ATTR")
@AssociationOverride(name = "pk.product", joinColumns = @JoinColumn(name = "product_id"))
@AssociationOverride(name = "pk.attribute", joinColumns = @JoinColumn(name = "attribute_id"))
public class TbTeamProdAttr {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private TbTeamProduct product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attribute_id", nullable = false)
  private TbTeamAttr attribute;

  @Column(nullable = false)
  private String attrValue;

}
