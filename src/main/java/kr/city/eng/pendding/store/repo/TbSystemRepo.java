package kr.city.eng.pendding.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.TbSystem;

public interface TbSystemRepo extends JpaRepository<TbSystem, String> {

  default String getVersion() {
    return findById(TbSystem.Key.VER.getProperty())
        .map(TbSystem::getValue)
        .orElse("");
  }

}
