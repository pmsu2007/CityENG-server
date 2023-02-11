package kr.city.eng.pendding.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.team.TbTeamPending;

public interface TbTeamPendingRepo extends
    JpaRepository<TbTeamPending, Long>,
    TbTeamPendingCustom {

}
