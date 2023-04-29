package kr.city.eng.pendding.dto;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.google.common.collect.Lists;

import kr.city.eng.pendding.store.entity.enums.AttrType;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.Data;

@Data
public class TeamAttrDto implements AppDto {

  protected Integer index;
  protected AttrType type;
  protected String name;

  protected List<String> values;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(name))
      throw ExceptionUtil.require("name");
  }

  public void setIndex(Integer val) {
    if (val == null)
      val = 0;
    this.index = val;
  }

  public void setType(AttrType val) {
    if (val == null)
      val = AttrType.STRING;
    this.type = val;
  }

  public void addValue(String... elements) {
    if (elements==null) return;

    if (values==null) {
      values = Lists.newArrayList();
    }
    for(String element:elements) {
      values.add(element);
    }
  }

}
