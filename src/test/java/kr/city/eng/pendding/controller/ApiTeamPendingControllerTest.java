package kr.city.eng.pendding.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import kr.city.eng.pendding.store.entity.team.TbTeamPlace;
import kr.city.eng.pendding.store.repo.TbTeamPendingProdRepo;
import kr.city.eng.pendding.store.repo.TbTeamPendingRepo;

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
  TbTeamPendingRepo store;
  @Autowired
  TbTeamPendingProdRepo storeHist;

  private TbTeam teamEntity;
  private MockMvc mockMvc;
  private final MockTeamProductService mockProduct = new MockTeamProductService();
  private final MockTeamPendingService mockService = new MockTeamPendingService();

  @BeforeEach
  public void setUp() {
    initialize.clearAll();
    this.teamEntity = initialize.initTeam();
    initialize.initTeamAndPlaceAndAttr(this.teamEntity, 2, 5);

    mockMvc = MockMvcBuilders.standaloneSetup(productController, controller)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();

    mockProduct.setUp(objectMapper, mockMvc);
    mockService.setUp(objectMapper, mockMvc);
  }

  private TeamProductDto getTeamProductDto() {
    TeamProductDto addDto = mockProduct.create();
    // 장소는 1개만 입력
    TbTeamPlace place = this.teamEntity.getTeamPlaces().get(0);
    addDto.addPlace(place.getId(), place.getName(), RandomUtils.nextInt(10));
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
    dto.setType(PendingType.MOVE);
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

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void pendingIn() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct product = mockProduct.add(teamId, getTeamProductDto());

    // 장소는 초기에 입력되지 않은 곳으로 지정
    TbTeamPlace teamPlace = this.teamEntity.getTeamPlaces().get(1);
    Place place = new Place(teamPlace.getId(), teamPlace.getName(), 0);

    TeamPending dto = getTeamPendingIn(product.getId(), place);

    TeamPending result = mockService.pending(teamId, dto);
    // 히스토리 확인
    TeamPending expected = mockService.getById(result.getId());
    assertEquals(expected, result);

    // 제품이 맞는지 확인
    assertEquals(1, result.size());
    TeamPendingProd pending = result.get(0);
    assertEquals(product.getId(), pending.getProductId());

    // toPlace에 있는지 확인
    Place prodToPlace = mockProduct.getByIdAndPlaceId(pending.getProductId(), pending.getToPlaceId());
    int toQuantity = pending.adjustToQuantity(PendingType.IN, pending.getToQuantity());
    assertEquals(toQuantity, prodToPlace.getQuantity());
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void pendingMove() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct product = mockProduct.add(teamId, getTeamProductDto());
    Place fromPlace = product.getPlaces().get(0);

    // 이동 장소는 초기에 입력되지 않은 곳으로 지정
    TbTeamPlace teamPlace = this.teamEntity.getTeamPlaces().get(1);
    Place toPlace = new Place(teamPlace.getId(), teamPlace.getName(), 0);

    // 이동
    TeamPending dto = getTeamPendingMove(product.getId(), fromPlace, toPlace);
    TeamPending result = mockService.pending(teamId, dto);
    // 히스토리 확인
    TeamPending expected = mockService.getById(result.getId());
    assertEquals(expected, result);

    // 제품이 맞는지 확인
    assertEquals(1, result.size());
    TeamPendingProd pending = result.get(0);
    assertEquals(product.getId(), pending.getProductId());

    // toPlace에 있는지 확인
    Place prodToPlace = mockProduct.getByIdAndPlaceId(pending.getProductId(), pending.getToPlaceId());
    int toQuantity = pending.adjustToQuantity(PendingType.IN, pending.getToQuantity());
    assertEquals(toQuantity, prodToPlace.getQuantity());

    // fromPlace에 있는지 확인
    Place prodFromPlace = mockProduct.getByIdAndPlaceId(pending.getProductId(), pending.getFromPlaceId());
    int fromQuantity = pending.adjustToQuantity(PendingType.OUT, pending.getFromQuantity());
    assertEquals(fromQuantity, prodFromPlace.getQuantity());
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void delete() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct product = mockProduct.add(teamId, getTeamProductDto());
    Place place = product.getPlaces().get(0);
    TeamPending dto = getTeamPendingIn(product.getId(), place);

    TeamPending result = mockService.pending(teamId, dto);
    // 히스토리 확인
    TeamPending expected = mockService.getById(result.getId());
    assertEquals(expected, result);
    // 제품 추가시 조정히스토리 있음.
    assertEquals(2, storeHist.count());
    assertEquals(2, store.count());

    // 삭제
    mockService.delete(teamId, result.getId());

    // 제품 추가시 조정히스토리 있음.
    assertEquals(1, storeHist.count());
    assertEquals(1, store.count());
  }

}
