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
import kr.city.eng.pendding.controller.mock.MockTeamPlaceService;
import kr.city.eng.pendding.dto.TeamPlace;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamPlace;
import kr.city.eng.pendding.store.mapper.TbTeamPlaceMapper;
import kr.city.eng.pendding.store.repo.TbTeamPlaceRepo;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTeamPlaceControllerTest {

  @Autowired
  TestStoreInitialize initialize;
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ApiTeamPlaceController controller;
  @Autowired
  TbTeamPlaceRepo store;
  @Autowired
  TbTeamPlaceMapper storeMapper;

  private TbTeam teamEntity;
  private MockMvc mockMvc;
  private final MockTeamPlaceService mockService = new MockTeamPlaceService();

  @BeforeEach
  public void setUp() {
    initialize.clearAll();
    this.teamEntity = initialize.initTeam();

    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();

    mockService.setUp(objectMapper, mockMvc);
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void create() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamPlace dto = mockService.add(teamId, mockService.create());

    TbTeamPlace result = store.findById(dto.getId()).get();
    assertEquals(dto, storeMapper.toDto(result));
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void getAll() throws Exception {
    Long teamId = this.teamEntity.getId();
    List<TeamPlace> lists = Lists.newArrayList();
    for (int i = 0; i < 10; i++) {
      lists.add(mockService.add(teamId, mockService.create()));
    }

    List<TeamPlace> result = mockService.getAll(teamId);
    assertEquals(lists, result);
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void getPage() throws Exception {
    Long teamId = this.teamEntity.getId();
    List<TeamPlace> lists = Lists.newArrayList();
    for (int i = 0; i < 10; i++) {
      lists.add(mockService.add(teamId, mockService.create()));
    }
    // 이름 오름차순(ASC)으로 정렬
    lists.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));

    LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("page", "0");
    params.add("size", "5");
    params.add("sort", "name,ASC");

    List<TeamPlace> result = mockService.getPage(teamId, params);
    assertEquals(lists.subList(0, 5), result);
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void getById() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamPlace dto = mockService.add(teamId, mockService.create());

    TeamPlace result = mockService.getById(dto.getId());
    assertEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void update() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamPlace dto = mockService.add(teamId, mockService.create());

    TeamPlace result = mockService.update(dto.getId(), dto);
    assertEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void delete() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamPlace dto = mockService.add(teamId, mockService.create());

    mockService.delete(dto.getId());

    Optional<TbTeamPlace> op = store.findById(dto.getId());
    assertFalse(op.isPresent());
  }

}
