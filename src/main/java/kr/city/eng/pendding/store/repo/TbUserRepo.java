package kr.city.eng.pendding.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.TbUser;

public interface TbUserRepo extends JpaRepository<TbUser, String> {

  boolean existsByEmail(String email);

}
