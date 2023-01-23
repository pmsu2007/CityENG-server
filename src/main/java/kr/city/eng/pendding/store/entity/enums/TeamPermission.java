package kr.city.eng.pendding.store.entity.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TeamPermission {
  ROLE("R"),
  PRODUCT("P"),
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

}
