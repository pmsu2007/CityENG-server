package kr.city.eng.pendding.store.repo;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.city.eng.pendding.store.entity.team.TbTeamProdAttr;

public interface TbTeamProdAttrRepo extends JpaRepository<TbTeamProdAttr, Long> {

  @Query("SELECT t.attrValue FROM TbTeamProdAttr t WHERE t.attribute.id=:id GROUP BY t.attrValue")
  Collection<String> findValuesByAttrGroupBy(@Param(value = "id") Long attributeId);

  Collection<TbTeamProdAttr> findByProductId(Long productId);

  Optional<TbTeamProdAttr> findByProductIdAndAttributeId(Long productId, Long attributeId);

  @Modifying(clearAutomatically = true)
  @Query("delete from TbTeamProdAttr t where t.attribute.id=:id")
  void deleteByAttributeId(@Param(value = "id") Long attributeId);

  @Modifying(clearAutomatically = true)
  @Query("delete from TbTeamProdAttr t where t.product.id=:id")
  void deleteByProductId(@Param(value = "id") Long productId);

}
