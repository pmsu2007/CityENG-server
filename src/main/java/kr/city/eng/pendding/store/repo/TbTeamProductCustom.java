package kr.city.eng.pendding.store.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.dto.TeamProductDto.Place;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamPlace;

public interface TbTeamProductCustom {

  Optional<TeamProduct> findDtoById(Long id);

  Optional<Place> findProductPlaceBy(Long productId, Long placeId);

  Page<TeamProduct> findDtoByTeam(TbTeam team, Pageable pageable);

  Page<TeamProduct> findDtoByTeam(TbTeam team, String value, Pageable pageable);

  Page<TeamProduct> findDtoByTeamPlace(TbTeamPlace place, String value, Pageable pageable);

}
