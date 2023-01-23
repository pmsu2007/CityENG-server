package kr.city.eng.pendding.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.team.TbTeamRole;

public interface TbTeamRoleRepo extends JpaRepository<TbTeamRole, Long> {

}
