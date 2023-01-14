package kr.city.eng.pendding.store.entity.team;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import kr.city.eng.pendding.store.entity.TbEntity;
import kr.city.eng.pendding.store.entity.TbImage;
import kr.city.eng.pendding.store.entity.TbUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_TEAM")
public class TbTeam extends TbEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(nullable = false)
  private String name;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "image_id")
  private TbImage image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private TbUser user;

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
  private List<TbTeamUser> teamUsers;

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
  private List<TbTeamPlace> teamPlaces;

}
