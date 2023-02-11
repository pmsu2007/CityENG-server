package kr.city.eng.pendding.store.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.team.TbTeamPending;
import kr.city.eng.pendding.store.entity.team.TbTeamPendingProd;

public interface TbTeamPendingProdRepo extends JpaRepository<TbTeamPendingProd, Long> {

  List<TbTeamPendingProd> findByPending(TbTeamPending pending);

  void deleteByPending(TbTeamPending pending);

}
