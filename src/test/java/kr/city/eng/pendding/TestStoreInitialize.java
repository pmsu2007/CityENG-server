package kr.city.eng.pendding;

import javax.transaction.Transactional;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.enums.AuthType;
import kr.city.eng.pendding.store.entity.enums.UserRole;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbUserRepo;

@Component
@SuppressWarnings({ "unused" })
public class TestStoreInitialize {

  @Autowired
  TbUserRepo storeUser;
  @Autowired
  TbTeamRepo storeTeam;

  public TbUser initAdminUser() {
    TbUser entity = new TbUser();
    entity.setId("admin");
    entity.setPassword("admin");
    entity.setAuthentication(AuthType.SIGNIN);
    entity.setRole(UserRole.ADMIN);
    entity.setEmail("admin@yourcompany.com");
    entity.setName("Administrator");
    entity.setSystem(true);
    entity.setCreatedAt(System.currentTimeMillis());
    entity.setUpdatedAt(System.currentTimeMillis());
    return storeUser.save(entity);
  }

  public TbTeam initTeam() {
    TbTeam entity = new TbTeam();
    entity.setName("Team-" + RandomStringUtils.randomAlphabetic(2));
    entity.setCreatedAt(System.currentTimeMillis());
    entity.setUpdatedAt(System.currentTimeMillis());
    return storeTeam.save(entity);
  }

  @Transactional
  public void clearAll() {
    storeTeam.deleteAllInBatch();
    storeUser.deleteAllInBatch();
  }

}
