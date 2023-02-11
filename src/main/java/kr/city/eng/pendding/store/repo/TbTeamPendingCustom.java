package kr.city.eng.pendding.store.repo;

import java.util.List;
import java.util.Set;

import kr.city.eng.pendding.dto.TeamPending;
import kr.city.eng.pendding.store.entity.enums.PendingType;

public interface TbTeamPendingCustom {

  List<TeamPending> findAllBy(Long teamId, String userId,
      Long startDateTime, Long endDateTime,
      Set<PendingType> types, Set<Long> placeIds);

}
