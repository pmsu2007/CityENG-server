package kr.city.eng.pendding.store.entity.team.key;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import lombok.Data;

@Data
@Embeddable
public class TeamUserKey implements Serializable {

  private static final long serialVersionUID = -8933905736181339729L;

  @ManyToOne
  private TbTeam team;
  @ManyToOne
  private TbUser user;

}
