package kr.city.eng.pendding.store.entity.team.key;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import kr.city.eng.pendding.store.entity.team.TbTeamPlace;
import kr.city.eng.pendding.store.entity.team.TbTeamProduct;
import lombok.Data;

@Data
@Embeddable
public class TeamProdPlaceKey implements Serializable {

  private static final long serialVersionUID = 8231197756411544860L;

  @ManyToOne
  private TbTeamProduct product;
  @ManyToOne
  private TbTeamPlace place;

}
