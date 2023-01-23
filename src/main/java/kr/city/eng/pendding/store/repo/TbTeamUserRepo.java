package kr.city.eng.pendding.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.team.TbTeamUser;

public interface TbTeamUserRepo extends JpaRepository<TbTeamUser, Long> {

}
