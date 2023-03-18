package kr.city.eng.pendding.store.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class TbUser extends TbDateEntity {

  @Id
  @EqualsAndHashCode.Include
  private String id;
  @Column(nullable = false)
  private boolean system;

  @Column(nullable = false)
  private String password;

  private AuthType authentication = AuthType.SIGNIN;
  private UserRole role = UserRole.USER;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false, unique = true)
  private String email;

  private String imageUrl;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private List<TbTeam> teams;

  @Column(nullable = false)
  private String apikey;

}
