package kr.city.eng.pendding.store.entity.team;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Lists;

import kr.city.eng.pendding.store.entity.TbDateEntity;
import kr.city.eng.pendding.store.entity.TbUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_TEAM")
public class TbTeam extends TbDateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(nullable = false)
  private String name;

  private String imageUrl;

  private String memo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private TbUser user;

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<TbTeamProduct> teamProducts = Lists.newArrayList();

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<TbTeamAttr> teamAttributes = Lists.newArrayList();

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<TbTeamPlace> teamPlaces = Lists.newArrayList();

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<TbTeamRole> teamRoles = Lists.newArrayList();

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
  private List<TbTeamUser> teamUsers = Lists.newArrayList();

}
