package kr.city.eng.pendding.store.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamAttr;

public interface TbTeamAttrRepo extends JpaRepository<TbTeamAttr, Long> {

  List<TbTeamAttr> findByTeam(TbTeam team);

  Page<TbTeamAttr> findByTeam(TbTeam team, Pageable pageable);

}
