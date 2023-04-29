package kr.city.eng.pendding.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

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
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import kr.city.eng.pendding.TestStoreInitialize;
import kr.city.eng.pendding.controller.mock.MockTeamAttrService;
import kr.city.eng.pendding.dto.TeamAttr;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamAttr;
import kr.city.eng.pendding.store.mapper.TbTeamAttrMapper;
import kr.city.eng.pendding.store.repo.TbTeamAttrRepo;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTeamAttrControllerTest {

  @Autowired
  TestStoreInitialize initialize;
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ApiTeamAttrController controller;
  @Autowired
  TbTeamAttrRepo store;
  @Autowired
  TbTeamAttrMapper storeMapper;

  private TbTeam teamEntity;
  private MockMvc mockMvc;
  private final MockTeamAttrService mockService = new MockTeamAttrService();

  @BeforeEach
  public void setUp() {
    initialize.clearAll();
    this.teamEntity = initialize.initTeam();

    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
        .build();

    mockService.setUp(objectMapper, mockMvc);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void create() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamAttr dto = mockService.add(teamId, mockService.createEnumAttr());

    TbTeamAttr result = store.findById(dto.getId()).get();
    assertEquals(dto, storeMapper.toDto(result));
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void getAll() throws Exception {
    Long teamId = this.teamEntity.getId();
    List<TeamAttr> lists = Lists.newArrayList();
    for (int i = 0; i < 10; i++) {
      lists.add(mockService.add(teamId, mockService.create(i)));
    }

    List<TeamAttr> result = mockService.getAll(teamId);
    assertEquals(lists, result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void getPage() throws Exception {
    Long teamId = this.teamEntity.getId();
    List<TeamAttr> lists = Lists.newArrayList();
    for (int i = 0; i < 10; i++) {
      lists.add(mockService.add(teamId, mockService.create(i)));
    }
    // 이름 오름차순(ASC)으로 정렬
    lists.sort((o1, o2) -> o1.getIndex().compareTo(o2.getIndex()));

    LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("page", "0");
    params.add("size", "5");
    params.add("sort", "index,ASC");

    List<TeamAttr> result = mockService.getPage(teamId, params);
    assertEquals(lists.subList(0, 5), result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void getById() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamAttr dto = mockService.add(teamId, mockService.create(0));

    TeamAttr result = mockService.getById(dto.getId());
    assertEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void update() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamAttr dto = mockService.add(teamId, mockService.create(0));

    dto.setName("no name");

    TeamAttr result = mockService.update(teamId, dto.getId(), dto);
    assertEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void delete() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamAttr dto = mockService.add(teamId, mockService.create(0));

    mockService.delete(teamId, dto.getId());

    Optional<TbTeamAttr> op = store.findById(dto.getId());
    assertFalse(op.isPresent());
  }

}
