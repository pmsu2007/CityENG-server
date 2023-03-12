package kr.city.eng.pendding.store.repo;

import static kr.city.eng.pendding.store.entity.team.QTbTeamProdAttr.tbTeamProdAttr;
import static kr.city.eng.pendding.store.entity.team.QTbTeamProdPlace.tbTeamProdPlace;
import static kr.city.eng.pendding.store.entity.team.QTbTeamProduct.tbTeamProduct;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QTuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.dto.TeamProductDto.Place;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.mapper.TbTeamProductMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class TbTeamProductCustomImpl implements TbTeamProductCustom {

  private final JPAQueryFactory queryFactory;
  private final TbTeamProductMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Optional<TeamProduct> findDtoById(Long id) {
    return getDtoById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Place> findProductPlaceBy(Long productId, Long placeId) {
    return getTeamProdPlaceQuery(productId, placeId).fetch()
        .stream().map(mapper::toTeamProdPlace)
        .findFirst();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TeamProduct> findDtoByTeam(TbTeam team, Pageable pageable) {
    return findDtoByTeam(team, "", pageable);
  }

  @Override
  @Transactional(readOnly = true)
  @SuppressWarnings("squid:S4449")
  public Page<TeamProduct> findDtoByTeam(TbTeam team, String value, Pageable pageable) {
    List<TeamProduct> contents = getDtoByTeam(team, value, pageable);
    JPAQuery<Tuple> coutnQuery = getTeamProductCountQuery(team, value);
    return PageableExecutionUtils.getPage(contents, pageable, () -> coutnQuery.fetch().size());
  }

  private Optional<TeamProduct> getDtoById(Long id) {
    QTuple select = getSelectTeamProduct();
    Map<Long, TeamProduct> map = queryFactory.select(select)
        .from(tbTeamProduct)
        .where(tbTeamProduct.id.eq(id))
        .fetch().stream()
        .map(mapper::toDto)
        .collect(Collectors.toMap(TeamProduct::getId, Function.identity()));

    getTeamProdPlaceQuery(map.keySet()).fetch()
        .forEach(it -> mapper.setTeamProdPlace(it, map));
    getTeamProdAttrQuery(map.keySet()).fetch()
        .forEach(it -> mapper.setTeamProdAttr(it, map));
    return map.values().stream().findFirst();
  }

  private List<TeamProduct> getDtoByTeam(TbTeam team, String value, Pageable pageable) {
    JPAQuery<Tuple> query = getTeamProductQuery(team, value, pageable);
    Map<Long, TeamProduct> map = query.fetch().stream()
        .map(mapper::toDto)
        .collect(Collectors.toMap(TeamProduct::getId, Function.identity()));

    getTeamProdPlaceQuery(map.keySet()).fetch()
        .forEach(it -> mapper.setTeamProdPlace(it, map));
    getTeamProdAttrQuery(map.keySet()).fetch()
        .forEach(it -> mapper.setTeamProdAttr(it, map));
    return Lists.newArrayList(map.values());
  }

  private JPAQuery<Tuple> getTeamProductQuery(TbTeam team, String value, Pageable pageable) {
    QTuple select = getSelectTeamProduct();
    return getTeamProductQuery(select, team, value, pageable);
  }

  private JPAQuery<Tuple> getTeamProductQuery(QTuple select, TbTeam team, String value, Pageable pageable) {
    JPAQuery<Tuple> query = queryFactory.select(select)
        .from(tbTeamProduct)
        .where(tbTeamProduct.team.eq(team));

    // 속성의 값을 비교할 경우는 tbTeamProdAttr도 조인 해줘야 함.
    if (StringUtils.hasLength(value)) {
      query.leftJoin(tbTeamProdAttr)
          .on(tbTeamProdAttr.product.id.eq(tbTeamProduct.id))
          .distinct();

      query.where(tbTeamProduct.barcode.likeIgnoreCase(value)
          .or(tbTeamProduct.name.likeIgnoreCase(value))
          .or(tbTeamProdAttr.attrValue.likeIgnoreCase(value)));
    }
    if (pageable != null) {
      query.offset(pageable.getOffset()) // 페이지 번호
          .limit(pageable.getPageSize()); // 페이지 사이즈
    }
    return query;
  }

  private JPAQuery<Tuple> getTeamProductCountQuery(TbTeam team, String value) {
    QTuple select = Projections.tuple(tbTeamProduct.id);
    return getTeamProductQuery(select, team, value, null);
  }

  private QTuple getSelectTeamProduct() {
    return Projections.tuple(
        tbTeamProduct.id, tbTeamProduct.barcode, tbTeamProduct.name,
        tbTeamProduct.imageUrl, tbTeamProduct.createdAt, tbTeamProduct.updatedAt);
  }

  private JPAQuery<Tuple> getTeamProdAttrQuery(Set<Long> ids) {
    QTuple select = Projections.tuple(tbTeamProdAttr.product.id,
        tbTeamProdAttr.attribute.id,
        tbTeamProdAttr.attribute.name,
        tbTeamProdAttr.attrValue);
    return queryFactory.select(select)
        .from(tbTeamProdAttr)
        .where(tbTeamProdAttr.product.id.in(ids));
  }

  private JPAQuery<Tuple> getTeamProdPlaceQuery(Set<Long> ids) {
    QTuple select = Projections.tuple(tbTeamProdPlace.product.id,
        tbTeamProdPlace.place.id,
        tbTeamProdPlace.place.name,
        tbTeamProdPlace.quantity);
    return queryFactory.select(select)
        .from(tbTeamProdPlace)
        .where(tbTeamProdPlace.product.id.in(ids));
  }

  private JPAQuery<Tuple> getTeamProdPlaceQuery(Long productId, Long placeId) {
    QTuple select = Projections.tuple(tbTeamProdPlace.product.id,
        tbTeamProdPlace.place.id,
        tbTeamProdPlace.place.name,
        tbTeamProdPlace.quantity);
    return queryFactory.select(select)
        .from(tbTeamProdPlace)
        .where(tbTeamProdPlace.product.id.eq(productId))
        .where(tbTeamProdPlace.place.id.eq(placeId));
  }

}
