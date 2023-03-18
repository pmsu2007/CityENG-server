package kr.city.eng.pendding.service;

import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import kr.city.eng.pendding.store.entity.enums.TeamPermission;
import kr.city.eng.pendding.store.repo.TbTeamUserRepo;
import kr.city.eng.pendding.util.AppUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiTeamPermission {

  private final TbTeamUserRepo store;

  private Table<Long, String, Set<TeamPermission>> caches = HashBasedTable.create();

  public void verify(Long teamId, TeamPermission permission) {
    String userId = AppUtil.getAuthUser();
    Set<TeamPermission> permissions = getTeamPermissions(teamId, userId);
    if (!permissions.contains(permission)) {
      String msg = "Access Permission(" + permission.name() + ") Denied";
      throw new AccessDeniedException(msg);
    }
  }

  private Set<TeamPermission> getTeamPermissions(Long teamId, String userId) {
    final Set<TeamPermission> permission = Sets.newHashSet();
    if (!caches.contains(teamId, userId)) {
      store.findByTeamAndUser(teamId, userId).ifPresent(entity -> {
        permission.addAll(entity.getTeamRole().getPermissions());
        caches.put(teamId, userId, permission);
      });
    } else {
      permission.addAll(caches.get(teamId, userId));
    }
    return permission;
  }

}
