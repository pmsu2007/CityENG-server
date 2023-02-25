package kr.city.eng.pendding.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.ObjectUtils;

import com.google.common.collect.Lists;

import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
public class TeamProductDto implements AppDto {

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class Place implements Comparable<Place> {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private int quantity;

    public boolean validate() {
      return !ObjectUtils.isEmpty(id) && !ObjectUtils.isEmpty(quantity);
    }

    @Override
    public int compareTo(Place o) {
      return id.compareTo(o.id);
    }
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class Attr implements Comparable<Attr> {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String value;

    public boolean validate() {
      return !ObjectUtils.isEmpty(id);
    }

    @Override
    public int compareTo(Attr o) {
      return id.compareTo(o.id);
    }
  }

  protected String name;
  protected String barcode;
  protected String imageUrl;

  protected List<Place> places = Lists.newArrayList();
  protected List<Attr> attributes = Lists.newArrayList();

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(name))
      throw ExceptionUtil.require("name");
    if (ObjectUtils.isEmpty(barcode))
      throw ExceptionUtil.require("barcode");
    if (ObjectUtils.isEmpty(places))
      throw ExceptionUtil.require("places");

    if (places.isEmpty() || !places.stream().allMatch(Place::validate)) {
      throw ExceptionUtil.require("places[*].placeId or places[*].quantity");
    }

    if (!attributes.isEmpty()
        && !attributes.stream().allMatch(Attr::validate)) {
      throw ExceptionUtil.require("attributes[*].attrId");
    }
  }

  public void validatePatch() {
    boolean isName = ObjectUtils.isEmpty(name);
    boolean isBarcode = ObjectUtils.isEmpty(barcode);
    boolean isAttr = ObjectUtils.isEmpty(attributes);

    if (isName && isBarcode && isAttr)
      throw ExceptionUtil.require("name , barcode or attributes");
  }

  public void addPlace(Long id, String name, int quantity) {
    this.places.add(new Place(id, name, quantity));
  }

  public void addAttribute(Long id, String name, String value) {
    this.attributes.add(new Attr(id, name, value));
  }

  public void sorted() {
    places = places.stream().sorted().collect(Collectors.toList());
    attributes = attributes.stream().sorted().collect(Collectors.toList());
  }

}
