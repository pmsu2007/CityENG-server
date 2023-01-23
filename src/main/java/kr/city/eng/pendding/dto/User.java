package kr.city.eng.pendding.dto;

import kr.city.eng.pendding.store.entity.enums.AuthType;
import kr.city.eng.pendding.store.entity.enums.UserRole;
import lombok.Data;

@Data
public class User {

  private String id;
  private boolean system;

  private AuthType authentication;
  private UserRole role;
  private String name;
  private String email;
  private String imageUrl;

}
