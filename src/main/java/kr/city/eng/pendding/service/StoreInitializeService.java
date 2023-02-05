package kr.city.eng.pendding.service;

import javax.persistence.EntityManagerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreInitializeService {

  private final EntityManagerFactory entityManagerFactory;

  @Transactional
  public void checkStoreSchema() {
  }

}
