package kr.city.eng.pendding.store.entity.enums;

import java.util.Set;

import com.google.common.collect.Sets;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TeamPermission {
  ROLE("R"),
  PRODUCT("P"),
  PLACE("PL"),
  ATTRIBUTE("A"),
  PENDING_IN("PI"),
  PENDING_OUT("PO"),
  PENDING_ADJUST("PA"),
  PENDING_MOVE("PM");

  private final String abbr;

  public String getAbbr() {
    return this.abbr;
  }

  public static TeamPermission abbrOf(String addr) {
    for (TeamPermission item : TeamPermission.values()) {
      if (addr.equals(item.getAbbr())) {
        return item;
      }
    }
    return null;
  }

  public static Set<TeamPermission> admin() {
    return Sets.newHashSet(ROLE, PRODUCT, PLACE, ATTRIBUTE,
        PENDING_IN, PENDING_OUT, PENDING_ADJUST, PENDING_MOVE);
  }

  public static Set<TeamPermission> member() {
    return Sets.newHashSet(PENDING_IN, PENDING_OUT, PENDING_ADJUST, PENDING_MOVE);
  }

}
