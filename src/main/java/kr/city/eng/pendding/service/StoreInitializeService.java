package kr.city.eng.pendding.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.enums.UserRole;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.repo.TbSystemRepo;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbUserRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Profile("!test")
@RequiredArgsConstructor
@Service(value = "storeInitializeService")
public class StoreInitializeService {

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  private class Ver implements Comparable<Ver> {
    private Integer mager = 0;
    private Integer minor = 0;
    private Integer patch = 0;
    private File file;

    @Override
    @SuppressWarnings("squid:S3358")
    public int compareTo(Ver o) {
      int index = this.mager.compareTo(o.mager);
      if (index == 0) {
        index = this.minor.compareTo(o.minor);
        if (index == 0) {
          index = this.patch.compareTo(o.patch);
        }
      }
      return index;
    }
  }

  private final EntityManagerFactory entityManagerFactory;
  private final BCryptPasswordEncoder passwordEncoder;

  private final TbSystemRepo storeSystem;
  private final TbUserRepo storeUser;
  private final TbTeamRepo storeTeam;

  @SuppressWarnings("squid:S2229")
  public void checkSchema() {
    checkAdminUser();

    Ver dbVer = getDbVersions();
    getSqlVersions(dbVer).forEach(this::excuteQuery);
  }

  private void excuteQuery(Ver ver) {
    EntityManager em = entityManagerFactory.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      StringBuilder sql = new StringBuilder();
      Files.readLines(ver.getFile(), StandardCharsets.UTF_8).forEach(line -> {
        sql.append(line);
        if (line.endsWith(";")) {
          sql.delete(sql.length() - 1, sql.length());
          // 쿼리 실행
          em.createNativeQuery(sql.toString()).executeUpdate();
          sql.delete(0, sql.length());
        }
      });
    } catch (Exception e) {
      // ignore
      tx.rollback();
    } finally {
      tx.commit();
      em.close();
    }
  }

  @Transactional
  public void checkAdminUser() {
    TbUser admin = storeUser.findById("admin").orElseGet(() -> {
      TbUser user = new TbUser();
      user.setId("admin");
      user.setPassword(passwordEncoder.encode("admin"));
      user.setRole(UserRole.ADMIN);
      user.setName("Administrator");
      user.setEmail("admin@yourcompany.com");
      user.setSystem(true);
      user.setCreatedAt(System.currentTimeMillis());
      user.setUpdatedAt(System.currentTimeMillis());
      return storeUser.save(user);
    });
    checkAdminTeam(admin);
  }

  @Transactional
  public void checkAdminTeam(TbUser admin) {
    if (storeTeam.findByUser(admin).isEmpty()) {
      TbTeam team = new TbTeam();
      team.setUser(admin);

      team.setName("도시ENG");
      team.setMemo("시스템에 의해 자동 생성된 팀");
      team.setCreatedAt(System.currentTimeMillis());
      team.setUpdatedAt(System.currentTimeMillis());
      storeTeam.save(team);
    }
  }

  @Transactional
  public Ver getDbVersions() {
    Ver ver = new Ver();
    String dbVersion = storeSystem.getVersion();
    String[] version = dbVersion.split("\\.");
    if (version.length == 3) {
      ver.mager = Integer.parseInt(version[0]);
      ver.minor = Integer.parseInt(version[1]);
      ver.patch = Integer.parseInt(version[2]);
    }
    return ver;
  }

  private List<Ver> getSqlVersions(Ver dbVer) {
    List<Ver> list = Lists.newArrayList();
    try {
      ClassPathResource resource = new ClassPathResource("sql");
      Stream.of(resource.getFile().listFiles()).forEach(file -> {
        String[] tokens = file.getName().split("\\.");
        if (tokens.length == 2 && tokens[1].equals("sql")) {
          String[] strVer = tokens[0].trim().split("_");
          if (strVer.length == 3) {
            Integer mager = Integer.parseInt(strVer[0]);
            Integer minor = Integer.parseInt(strVer[1]);
            Integer patch = Integer.parseInt(strVer[2]);
            Ver ver = new Ver(mager, minor, patch, file);
            if (ver.compareTo(dbVer) > 0) {
              list.add(ver);
            }
          }
        }
      });
      // 정렬
      return list.stream().sorted().collect(Collectors.toList());
    } catch (Exception e) {
      return list;
    }
  }

}
