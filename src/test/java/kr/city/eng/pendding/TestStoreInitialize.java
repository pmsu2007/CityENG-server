package kr.city.eng.pendding;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.enums.AuthType;
import kr.city.eng.pendding.store.entity.enums.UserRole;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamAttr;
import kr.city.eng.pendding.store.entity.team.TbTeamPlace;
import kr.city.eng.pendding.store.entity.team.TbTeamProdAttr;
import kr.city.eng.pendding.store.entity.team.TbTeamProdPlace;
import kr.city.eng.pendding.store.entity.team.TbTeamProduct;
import kr.city.eng.pendding.store.repo.TbTeamAttrRepo;
import kr.city.eng.pendding.store.repo.TbTeamPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdAttrRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamProductRepo;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbUserRepo;

@Component
@SuppressWarnings({ "unused" })
public class TestStoreInitialize {

  @Autowired
  TbUserRepo storeUser;
  @Autowired
  TbTeamRepo storeTeam;
  @Autowired
  TbTeamPlaceRepo storeTeamPlace;
  @Autowired
  TbTeamAttrRepo storeTeamAttr;
  @Autowired
  TbTeamProductRepo storeTeamProduct;
  @Autowired
  TbTeamProdPlaceRepo storeTeamProdPlace;
  @Autowired
  TbTeamProdAttrRepo storeTeamProdAttr;

  @Transactional
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

  @Transactional
  public TbTeam initTeam() {
    TbTeam entity = new TbTeam();
    entity.setUser(initAdminUser());
    entity.setName("Team-" + RandomStringUtils.randomAlphabetic(2));
    entity.setCreatedAt(System.currentTimeMillis());
    entity.setUpdatedAt(System.currentTimeMillis());
    return storeTeam.saveAndFlush(entity);
  }

  @Transactional
  public TbTeam initTeamAndPlaceAndAttr(int placeCnt, int attrCnt) {
    TbTeam entity = initTeam();
    List<TbTeamPlace> places = entity.getTeamPlaces();
    List<TbTeamAttr> attrs = entity.getTeamAttributes();
    entity.getTeamAttributes();
    for (int j = 0; j < placeCnt; j++) {
      places.add(initTeamPlace(entity, j));
    }
    for (int j = 0; j < attrCnt; j++) {
      attrs.add(initTeamAttrr(entity, j));
    }
    return entity;
  }

  @Transactional
  public TbTeamPlace initTeamPlace(TbTeam team, int index) {
    TbTeamPlace entity = new TbTeamPlace();
    entity.setName("TeamPalce-" + index);
    entity.setCreatedAt(System.currentTimeMillis());
    entity.setUpdatedAt(System.currentTimeMillis());
    entity.setTeam(team);
    return storeTeamPlace.saveAndFlush(entity);
  }

  @Transactional
  public TbTeamAttr initTeamAttrr(TbTeam team, int index) {
    TbTeamAttr entity = new TbTeamAttr();
    entity.setName("TeamAttr-" + index);
    entity.setIndex(index);
    entity.setCreatedAt(System.currentTimeMillis());
    entity.setUpdatedAt(System.currentTimeMillis());
    entity.setTeam(team);
    return storeTeamAttr.saveAndFlush(entity);
  }

  @Transactional
  public void clearAll() {
    storeTeam.deleteAllInBatch();
    storeUser.deleteAllInBatch();
  }

}
