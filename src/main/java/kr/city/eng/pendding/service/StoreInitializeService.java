package kr.city.eng.pendding.service;

import javax.persistence.EntityManagerFactory;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreInitializeService {

  private final EntityManagerFactory entityManagerFactory;

  public void checkStoreSchema() {
  }

}
