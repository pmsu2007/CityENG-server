package kr.city.eng.pendding.store.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.city.eng.pendding.store.entity.team.TbTeamPending;
import kr.city.eng.pendding.store.entity.team.TbTeamPendingProd;

public interface TbTeamPendingProdRepo extends JpaRepository<TbTeamPendingProd, Long> {

  List<TbTeamPendingProd> findByPending(TbTeamPending pending);

  void deleteByPending(TbTeamPending pending);

  @Modifying(clearAutomatically = true)
  @Query("update TbTeamPendingProd t set t.product.id=null where t.product.id=:productId")
  void setProductNull(@Param(value = "productId") Long productId);

  @Modifying(clearAutomatically = true)
  @Query("update TbTeamPendingProd t set t.toPlace.id=null where t.toPlace.id=:placeId")
  void setToPlaceNull(@Param(value = "placeId") Long placeId);

  @Modifying(clearAutomatically = true)
  @Query("update TbTeamPendingProd t set t.fromPlace.id=null where t.fromPlace.id=:placeId")
  void setFromPlaceNull(@Param(value = "placeId") Long placeId);
}
