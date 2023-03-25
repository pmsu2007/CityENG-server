package kr.city.eng.pendding;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.enums.AuthType;
import kr.city.eng.pendding.store.entity.enums.UserRole;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamAttr;
import kr.city.eng.pendding.store.entity.team.TbTeamPlace;
import kr.city.eng.pendding.store.entity.team.TbTeamRole;
import kr.city.eng.pendding.store.entity.team.TbTeamUser;
import kr.city.eng.pendding.store.repo.TbTeamAttrRepo;
import kr.city.eng.pendding.store.repo.TbTeamPendingProdRepo;
import kr.city.eng.pendding.store.repo.TbTeamPendingRepo;
import kr.city.eng.pendding.store.repo.TbTeamPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdAttrRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamProductRepo;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbTeamRoleRepo;
import kr.city.eng.pendding.store.repo.TbTeamUserRepo;
import kr.city.eng.pendding.store.repo.TbUserRepo;
import kr.city.eng.pendding.util.AppUtil;

@Component
@SuppressWarnings({ "unused" })
public class TestStoreInitialize {

  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  TbUserRepo storeUser;
  @Autowired
  TbTeamRepo storeTeam;
  @Autowired
  TbTeamRoleRepo storeTeamRole;
  @Autowired
  TbTeamUserRepo storeTeamUser;
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
  @Autowired
  TbTeamPendingProdRepo storeHist;
  @Autowired
  TbTeamPendingRepo storePending;

  public static final String API_KEY = AppUtil.genApiKey("admin", UserRole.ADMIN);

  @Transactional
  public TbUser initAdminUser() {
    TbUser entity = new TbUser();
    entity.setId("admin");
    entity.setApikey(API_KEY);
    entity.setPassword(passwordEncoder.encode("admin"));
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
  public TbTeam initTeamOnly() {
    TbUser adminUser = initAdminUser();
    TbTeam entity = new TbTeam();
    entity.setUser(adminUser);
    entity.setName("Team-" + RandomStringUtils.randomAlphabetic(2));
    entity.setCreatedAt(System.currentTimeMillis());
    entity.setUpdatedAt(System.currentTimeMillis());
    return storeTeam.save(entity);
  }

  @Transactional
  public TbTeam initTeam() {
    TbUser adminUser = initAdminUser();
    TbTeam entity = initTeamOnly();

    // team role 생성
    List<TbTeamRole> roles = initTeamRole(entity);
    entity.setTeamRoles(roles);
    // team user 생성
    TbTeamUser teamUser = initTeamUser(entity, adminUser, roles.get(1));
    entity.setTeamUsers(Lists.newArrayList(teamUser));
    return entity;
  }

  @Transactional
  public List<TbTeamRole> initTeamRole(TbTeam team) {
    List<TbTeamRole> list = Lists.newArrayList();
    TbTeamRole entity = TbTeamRole.member();
    entity.setTeam(team);
    list.add(storeTeamRole.save(entity));

    entity = TbTeamRole.admin();
    entity.setTeam(team);
    list.add(storeTeamRole.save(entity));
    return list;
  }

  @Transactional
  public TbTeamUser initTeamUser(TbTeam team, TbUser user, TbTeamRole role) {
    TbTeamUser entity = new TbTeamUser();
    entity.setTeam(team);
    entity.setUser(user);
    entity.setTeamRole(role);
    entity.setCreatedAt(System.currentTimeMillis());
    entity.setUpdatedAt(System.currentTimeMillis());
    storeTeamUser.save(entity);
    return entity;
  }

  @Transactional
  public TbTeam initTeamAndPlaceAndAttr(TbTeam team, int placeCnt, int attrCnt) {
    List<TbTeamPlace> places = team.getTeamPlaces();
    List<TbTeamAttr> attrs = team.getTeamAttributes();
    team.getTeamAttributes();
    for (int j = 0; j < placeCnt; j++) {
      places.add(initTeamPlace(team, j));
    }
    for (int j = 0; j < attrCnt; j++) {
      attrs.add(initTeamAttrr(team, j));
    }
    return team;
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
    storeHist.deleteAllInBatch();
    storePending.deleteAllInBatch();

    storeTeamProdAttr.deleteAllInBatch();
    storeTeamProdPlace.deleteAllInBatch();
    storeTeamProduct.deleteAllInBatch();

    storeTeamAttr.deleteAllInBatch();
    storeTeamPlace.deleteAllInBatch();
    storeTeamUser.deleteAllInBatch();
    storeTeamRole.deleteAllInBatch();
    storeTeam.deleteAllInBatch();
    storeUser.deleteAllInBatch();
  }
}
