package kr.city.eng.pendding.store.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.team.TbTeam;

public interface TbTeamRepo extends JpaRepository<TbTeam, Long> {

  List<TbTeam> findByUser(TbUser user);

}
