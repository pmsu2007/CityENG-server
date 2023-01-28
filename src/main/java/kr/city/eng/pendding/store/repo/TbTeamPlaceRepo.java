package kr.city.eng.pendding.store.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamPlace;

public interface TbTeamPlaceRepo extends JpaRepository<TbTeamPlace, Long> {

  List<TbTeamPlace> findByTeam(TbTeam team);

  Page<TbTeamPlace> findByTeam(TbTeam team, Pageable pageable);

}
