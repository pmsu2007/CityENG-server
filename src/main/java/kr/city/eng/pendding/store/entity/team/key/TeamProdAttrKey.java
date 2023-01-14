package kr.city.eng.pendding.store.entity.team.key;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import kr.city.eng.pendding.store.entity.team.TbTeamAttr;
import kr.city.eng.pendding.store.entity.team.TbTeamProduct;
import lombok.Data;

@Data
@Embeddable
public class TeamProdAttrKey implements Serializable {

  private static final long serialVersionUID = -2628384172508844123L;

  @ManyToOne
  private TbTeamProduct product;
  @ManyToOne
  private TbTeamAttr attribute;

}
