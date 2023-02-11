package kr.city.eng.pendding.store.repo;

import static kr.city.eng.pendding.store.entity.team.QTbTeamPending.tbTeamPending;
import static kr.city.eng.pendding.store.entity.team.QTbTeamPendingProd.tbTeamPendingProd;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QTuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.city.eng.pendding.dto.TeamPending;
import kr.city.eng.pendding.dto.TeamPendingProd;
import kr.city.eng.pendding.store.entity.enums.PendingType;
import kr.city.eng.pendding.store.mapper.TbTeamPendingMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class TbTeamPendingCustomImpl implements TbTeamPendingCustom {

  private final JPAQueryFactory queryFactory;
  private final TbTeamPendingMapper mapper;

  @Override
  @Transactional
  public List<TeamPending> findAllBy(Long teamId, String userId,
      Long startDateTime, Long endDateTime,
      Set<PendingType> types, Set<Long> placeIds) {
    JPAQuery<Tuple> query = queryFactory
        .select(getSelect())
        .from(tbTeamPending)
        .leftJoin(tbTeamPendingProd)
        .on(tbTeamPending.id.eq(tbTeamPendingProd.pending.id))
        .fetchJoin();
    setWhere(query, teamId, userId, startDateTime, endDateTime, types, placeIds);

    Map<Long, TeamPending> map = Maps.newLinkedHashMap();
    query.fetch().forEach(it -> {
      TeamPending pending = mapper.toDto(it);
      TeamPendingProd info = mapper.toInfoDto(it);
      map.computeIfAbsent(pending.getId(), k -> pending).add(info);
    });
    return Lists.newArrayList(map.values());
  }

  private QTuple getSelect() {
    return Projections.tuple(
        tbTeamPending.id, tbTeamPending.type, tbTeamPending.createdAt,
        tbTeamPending.memo, tbTeamPending.team.id, tbTeamPending.user.id,
        tbTeamPendingProd.id, tbTeamPendingProd.product.id, tbTeamPendingProd.quantity,
        tbTeamPendingProd.toPlace.id, tbTeamPendingProd.toQuantity,
        tbTeamPendingProd.fromPlace.id, tbTeamPendingProd.fromQuantity);
  }

  private JPAQuery<Tuple> setWhere(JPAQuery<Tuple> query,
      Long teamId, String userId,
      Long startDateTime, Long endDateTime,
      Set<PendingType> types, Set<Long> placeIds) {
    query.where(tbTeamPending.team.id.eq(teamId));
    if (!ObjectUtils.isEmpty(userId)) {
      query.where(tbTeamPending.user.id.eq(userId));
    }
    if (!ObjectUtils.isEmpty(startDateTime)) {
      query.where(tbTeamPending.createdAt.goe(startDateTime));
    }
    if (!ObjectUtils.isEmpty(endDateTime)) {
      query.where(tbTeamPending.createdAt.loe(endDateTime));
    }
    if (!ObjectUtils.isEmpty(types)) {
      query.where(tbTeamPending.type.in(types));
    }
    if (!ObjectUtils.isEmpty(placeIds)) {
      query.where(
          tbTeamPendingProd.toPlace.id.in(placeIds)
              .or(tbTeamPendingProd.fromPlace.id.in(placeIds)));
    }
    return query;
  }

}
