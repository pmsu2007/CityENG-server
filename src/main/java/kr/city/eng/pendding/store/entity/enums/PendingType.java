package kr.city.eng.pendding.store.entity.enums;

public enum PendingType {
  IN,
  OUT,
  ADJUST,
  MOVE;

  public TeamPermission toTeamPermission() {
    switch (this) {
      case OUT:
        return TeamPermission.PENDING_OUT;
      case ADJUST:
        return TeamPermission.PENDING_ADJUST;
      case MOVE:
        return TeamPermission.PENDING_MOVE;
      default:
        return TeamPermission.PENDING_IN;
    }
  }
}
