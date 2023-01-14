package kr.city.eng.pendding.store.entity.team;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kr.city.eng.pendding.store.entity.TbEntity;
import kr.city.eng.pendding.store.entity.TbUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_TEAM_USER")
public class TbTeamUser extends TbEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id", nullable = false)
  private TbTeam team;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private TbUser user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "team_role_id", nullable = false)
  private TbTeamRole teamRole;

}
