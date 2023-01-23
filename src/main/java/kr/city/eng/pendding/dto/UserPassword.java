package kr.city.eng.pendding.dto;

import org.springframework.util.ObjectUtils;

import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPassword {
  private String newPassword;
  private String oldPassword;

  public UserPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public void validate() {
    if (ObjectUtils.isEmpty(oldPassword))
      throw ExceptionUtil.require("oldPassword");
    if (ObjectUtils.isEmpty(newPassword))
      throw ExceptionUtil.require("newPassword");
  }

}
