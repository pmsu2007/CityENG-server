package kr.city.eng.pendding.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.team.TbTeamProduct;

public interface TbTeamProductRepo extends JpaRepository<TbTeamProduct, Long> {

}
