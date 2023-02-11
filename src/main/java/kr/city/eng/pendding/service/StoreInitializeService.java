package kr.city.eng.pendding.service;

import javax.persistence.EntityManagerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.city.eng.pendding.store.repo.TbSystemRepo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreInitializeService {

  private final EntityManagerFactory entityManagerFactory;
  private final TbSystemRepo storeSystem;

  @Transactional
  public void checkSchema() {
    String version = storeSystem.getVersion();
    // TODO: 버전에 따른 sql 실행
  }

}
