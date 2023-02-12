package kr.city.eng.pendding.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.TbSystem;

public interface TbSystemRepo extends JpaRepository<TbSystem, String> {

  default String getProperty(String key) {
    return findById(key).map(TbSystem::getPropVal).orElse("");
  }

  default String getVersion() {
    return getProperty(TbSystem.Prop.VER.getProperty());
  }

}
