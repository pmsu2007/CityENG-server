package kr.city.eng.pendding.store.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.team.TbTeamUser;

public interface TbTeamUserRepo extends JpaRepository<TbTeamUser, Long> {

  List<TbTeamUser> findByUser(TbUser user);

  @Query(value = "select t from TbTeamUser t where t.team.id = :teamId and t.user.id = :userId")
  Optional<TbTeamUser> findByTeamAndUser(
      @Param(value = "teamId") Long teamId,
      @Param(value = "userId") String userId);

}
