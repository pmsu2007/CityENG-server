package kr.city.eng.pendding.store.entity.team;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.common.collect.Sets;

import kr.city.eng.pendding.store.converter.TeamPermissionConverter;
import kr.city.eng.pendding.store.entity.TbDateEntity;
import kr.city.eng.pendding.store.entity.enums.TeamPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_TEAM_ROLE")
public class TbTeamRole extends TbDateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;
  private boolean system;

  @Column(nullable = false)
  private String name;

  @Convert(converter = TeamPermissionConverter.class)
  private Set<TeamPermission> permissions = Sets.newHashSet();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id", nullable = false)
  private TbTeam team;

  public static TbTeamRole admin() {
    TbTeamRole entity = new TbTeamRole();
    entity.setSystem(true);
    entity.setName("admin");
    entity.setPermissions(TeamPermission.admin());

    long timestamp = System.currentTimeMillis();
    entity.setCreatedAt(timestamp);
    entity.setUpdatedAt(timestamp);
    return entity;
  }

  public static TbTeamRole member() {
    TbTeamRole entity = new TbTeamRole();
    entity.setSystem(true);
    entity.setName("member");
    entity.setPermissions(TeamPermission.member());

    long timestamp = System.currentTimeMillis();
    entity.setCreatedAt(timestamp);
    entity.setUpdatedAt(timestamp);
    return entity;
  }

}
