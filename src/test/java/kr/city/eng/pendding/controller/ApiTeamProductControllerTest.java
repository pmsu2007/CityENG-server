package kr.city.eng.pendding.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
import kr.city.eng.pendding.controller.mock.MockTeamProductService;
import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.dto.TeamProductDto;
import kr.city.eng.pendding.dto.TeamProductDto.Attr;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.mapper.TbTeamProductMapper;
import kr.city.eng.pendding.store.repo.TbTeamProdAttrRepo;
import kr.city.eng.pendding.store.repo.TbTeamProdPlaceRepo;
import kr.city.eng.pendding.store.repo.TbTeamProductRepo;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTeamProductControllerTest {

  @Autowired
  TestStoreInitialize initialize;
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ApiTeamProductController controller;
  @Autowired
  TbTeamProductRepo store;
  @Autowired
  TbTeamProductMapper storeMapper;

  @Autowired
  TbTeamProdPlaceRepo storeTeamProdPlace;
  @Autowired
  TbTeamProdAttrRepo storeTeamProdAttr;

  private TbTeam teamEntity;
  private MockMvc mockMvc;
  private final MockTeamProductService mockService = new MockTeamProductService();

  @BeforeEach
  public void setUp() {
    initialize.clearAll();
    this.teamEntity = initialize.initTeam();
    initialize.initTeamAndPlaceAndAttr(this.teamEntity, 2, 5);

    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();

    mockService.setUp(objectMapper, mockMvc);
  }

  private TeamProductDto getTeamProductDto() {
    TeamProductDto addDto = mockService.create();
    this.teamEntity.getTeamPlaces().forEach(it -> {
      addDto.addPlace(it.getId(), it.getName(), RandomUtils.nextInt(10));
    });
    this.teamEntity.getTeamAttributes().forEach(it -> {
      addDto.addAttribute(it.getId(), it.getName(), "value:" + RandomUtils.nextInt(50));
    });
    return addDto;
  }

  private void assertDtoEquals(TeamProduct expected, TeamProduct actual) {
    expected.sorted();
    actual.sorted();
    assertEquals(expected, actual);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void create() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct dto = mockService.add(teamId, getTeamProductDto());

    TeamProduct result = store.findDtoById(dto.getId()).get();
    assertDtoEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void getById() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct dto = mockService.add(teamId, getTeamProductDto());

    TeamProduct result = mockService.getById(dto.getId());
    assertDtoEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void update() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct dto = mockService.add(teamId, getTeamProductDto());

    // 수정 내용
    dto.setName("Update-" + dto.getName());
    Attr attr = dto.getAttributes().get(0);
    attr.setValue("value:" + RandomUtils.nextInt(100));

    // 수정된 내용만 서버 전송
    TeamProduct newDto = new TeamProduct();
    newDto.setName(dto.getName());
    newDto.addAttribute(attr.getId(), attr.getName(), attr.getValue());

    TeamProduct result = mockService.update(teamId, dto.getId(), newDto);
    assertDtoEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void delete() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamProduct dto = mockService.add(teamId, getTeamProductDto());

    mockService.delete(teamId, dto.getId());

    assertFalse(store.findById(dto.getId()).isPresent());
    assertEquals(0, storeTeamProdPlace.findByProductId(dto.getId()).size());
    assertEquals(0, storeTeamProdAttr.findByProductId(dto.getId()).size());
  }

}
