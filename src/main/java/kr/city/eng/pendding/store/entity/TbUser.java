package kr.city.eng.pendding.store.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import kr.city.eng.pendding.store.entity.enums.AuthType;
import kr.city.eng.pendding.store.entity.enums.UserRole;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_USER")
public class TbUser extends TbEntity {

  @Id
  @EqualsAndHashCode.Include
  private String id;
  @Column(nullable = false)
  private String password;

  private AuthType authentication = AuthType.SIGNIN;
  private UserRole role = UserRole.USER;
  private String name;
  private String email;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "image_id")
  private TbImage image;

  @OneToMany(mappedBy = "user")
  private List<TbTeam> teams;

}
