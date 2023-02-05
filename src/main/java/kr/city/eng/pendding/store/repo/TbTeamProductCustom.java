package kr.city.eng.pendding.store.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.store.entity.team.TbTeam;

public interface TbTeamProductCustom {

  Optional<TeamProduct> findDtoById(Long id);

  Page<TeamProduct> findDtoByTeam(TbTeam team, Pageable pageable);

  Page<TeamProduct> findDtoByTeam(TbTeam team, String value, Pageable pageable);

}
