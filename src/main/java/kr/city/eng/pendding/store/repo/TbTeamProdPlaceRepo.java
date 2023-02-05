package kr.city.eng.pendding.store.repo;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.city.eng.pendding.store.entity.team.TbTeamProdPlace;

public interface TbTeamProdPlaceRepo extends JpaRepository<TbTeamProdPlace, Long> {

  Collection<TbTeamProdPlace> findByProductId(Long productId);

  @Modifying(clearAutomatically = true)
  @Query("delete from TbTeamProdPlace t where t.place.id=:id")
  void deleteByPlaceId(@Param(value = "id") Long placeId);

  @Modifying(clearAutomatically = true)
  @Query("delete from TbTeamProdPlace t where t.product.id=:id")
  void deleteByProductId(@Param(value = "id") Long productId);

}
