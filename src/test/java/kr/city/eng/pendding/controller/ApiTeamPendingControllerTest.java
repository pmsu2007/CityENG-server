package kr.city.eng.pendding.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.city.eng.pendding.TestStoreInitialize;
import kr.city.eng.pendding.controller.mock.MockTeamPendingService;
import kr.city.eng.pendding.controller.mock.MockTeamProductService;
import kr.city.eng.pendding.dto.TeamPending;
import kr.city.eng.pendding.dto.TeamPendingProd;
import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.dto.TeamProductDto;
import kr.city.eng.pendding.dto.TeamProductDto.Place;
import kr.city.eng.pendding.store.entity.enums.PendingType;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamPendingProd;
import kr.city.eng.pendding.store.entity.team.TbTeamProdPlace;
import kr.city.eng.pendding.store.repo.TbTeamPendingProdRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdPlaceRepo;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTeamPendingControllerTest {

  @Autowired
  TestStoreInitialize initialize;
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ApiTeamPendingController controller;
  @Autowired
  ApiTeamProductController productController;

  @Autowired
  TbTeamPendingProdRepo storeHist;
  @Autowired
  TbTeamProdPlaceRepo storeProdPlace;

  private TbTeam teamEntity;
  private MockMvc mockMvc;
  private final MockTeamProductService mockProduct = new MockTeamProductService();
  private final MockTeamPendingService mockService = new MockTeamPendingService();

  @BeforeEach
  public void setUp() {
    initialize.clearAll();
    this.teamEntity = initialize.initTeamAndPlaceAndAttr(2, 5);

    mockMvc = MockMvcBuilders.standaloneSetup(productController, controller)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();

    mockProduct.setUp(objectMapper, mockMvc);
    mockService.setUp(objectMapper, mockMvc);
  }

  private TeamProductDto getTeamProductDto() {
    TeamProductDto addDto = mockProduct.create();
    this.teamEntity.getTeamPlaces().forEach(it -> {
      addDto.addPlace(it.getId(), it.getName(), RandomUtils.nextInt(10));
    });
    this.teamEntity.getTeamAttributes().forEach(it -> {
      addDto.addAttribute(it.getId(), it.getName(), "value:" + RandomUtils.nextInt(50));
    });
    return addDto;
  }

  private TeamPending getTeamPendingIn(Long productId, Place place) {
    TeamPending dto = new TeamPending();
    dto.setType(PendingType.IN);
    dto.setCreatedAt(System.currentTimeMillis());

    TeamPendingProd prodDto = new TeamPendingProd();
    prodDto.setQuantity(5);
    prodDto.setProductId(productId);
    prodDto.setToPlaceId(place.getId());
    prodDto.setFromPlaceId(place.getId());
    prodDto.setFromQuantity(place.getQuantity());
    prodDto.setToQuantity(place.getQuantity());

    dto.add(prodDto);
    return dto;
  }

  private TeamPending getTeamPendingMove(Long productId, Place fromPlace, Place toPlace) {
    TeamPending dto = new TeamPending();
    dto.setType(PendingType.IN);
    dto.setCreatedAt(System.currentTimeMillis());

    TeamPendingProd prodDto = new TeamPendingProd();
    prodDto.setQuantity(5);
    prodDto.setProductId(productId);
    prodDto.setFromPlaceId(fromPlace.getId());
    prodDto.setFromQuantity(fromPlace.getQuantity());
    prodDto.setToPlaceId(toPlace.getId());
    prodDto.setToQuantity(toPlace.getQuantity());

    dto.add(prodDto);
    return dto;
  }

  private void assertPendingDto(TeamPending expected, TeamPending actual) {
    assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
    assertEquals(expected.getProducts().size(), actual.getProducts().size());

    int size = expected.size();
    for (int i = 0; i < size; i++) {
      assertEquals(expected.get(i).getProductId(), actual.get(i).getProductId());
      assertEquals(expected.get(i).getQuantity(), actual.get(i).getQuantity());
      assertEquals(expected.get(i).getFromPlaceId(), actual.get(i).getFromPlaceId());
      assertEquals(expected.get(i).getToPlaceId(), actual.get(i).getToPlaceId());
    }
  }

  private void assertPendingResult(TeamPendingProd pending, TbTeamPendingProd histProd) {
    assertEquals(pending.getProductId(), histProd.getProduct().getId());
    assertEquals(pending.getFromPlaceId(), histProd.getFromPlace().getId());
    assertEquals(pending.getToPlaceId(), histProd.getToPlace().getId());
    assertEquals(pending.getQuantity(), histProd.getQuantity());
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void pendingIn() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct product = mockProduct.add(teamId, getTeamProductDto());
    Place place = product.getPlaces().get(0);
    TeamPending dto = getTeamPendingIn(product.getId(), place);

    TeamPending result = mockService.pending(teamId, dto);
    TeamPendingProd pending = result.get(0);
    assertPendingDto(dto, result);

    // TbTeamProdPlace에 있는지 확인
    TbTeamProdPlace prodPlace = storeProdPlace
        .findByProductIdAndPlaceId(pending.getProductId(), pending.getToPlaceId())
        .orElseThrow();

    int expected = pending.adjustToQuantity(PendingType.IN, pending.getFromQuantity());
    assertEquals(expected, prodPlace.getQuantity());

    // 히스토리에 있는지 확인
    List<TbTeamPendingProd> hists = storeHist.findAll();
    assertEquals(1, hists.size());
    assertPendingResult(pending, hists.get(0));
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void pendingMove() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct product = mockProduct.add(teamId, getTeamProductDto());
    Place fromPlace = product.getPlaces().get(0);
    Place toPlace = product.getPlaces().get(1);
    TeamPending inDto = getTeamPendingIn(product.getId(), fromPlace);

    // 입고
    inDto = mockService.pending(teamId, inDto);
    fromPlace.setQuantity(fromPlace.getQuantity() + inDto.get(0).getQuantity());

    // 이동
    TeamPending dto = getTeamPendingMove(product.getId(), fromPlace, toPlace);
    TeamPending result = mockService.pending(teamId, dto);
    TeamPendingProd pending = result.get(0);
    assertPendingDto(dto, result);

    // toPlace에 있는지 확인
    TbTeamProdPlace prodToPlace = storeProdPlace
        .findByProductIdAndPlaceId(pending.getProductId(), pending.getToPlaceId())
        .orElseThrow();
    int expected = pending.adjustToQuantity(PendingType.IN, pending.getFromQuantity());
    assertEquals(expected, prodToPlace.getQuantity());

    // fromPlace에 있는지 확인
    TbTeamProdPlace prodFromPlace = storeProdPlace
        .findByProductIdAndPlaceId(pending.getProductId(), pending.getToPlaceId())
        .orElseThrow();
    expected = pending.adjustToQuantity(PendingType.OUT, pending.getFromQuantity());
    assertEquals(expected, prodFromPlace.getQuantity());

    // 히스토리에 있는지 확인
    List<TbTeamPendingProd> hists = storeHist.findAll();
    assertEquals(2, hists.size());
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void getAll() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct dto = mockProduct.add(teamId, getTeamProductDto());

  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void getById() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct dto = mockProduct.add(teamId, getTeamProductDto());

  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void delete() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct dto = mockProduct.add(teamId, getTeamProductDto());

  }

}
