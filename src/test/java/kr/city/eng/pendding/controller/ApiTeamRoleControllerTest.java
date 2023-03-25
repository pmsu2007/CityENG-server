package kr.city.eng.pendding.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import kr.city.eng.pendding.TestStoreInitialize;
import kr.city.eng.pendding.controller.mock.MockTeamRoleService;
import kr.city.eng.pendding.dto.TeamRole;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.entity.team.TbTeamRole;
import kr.city.eng.pendding.store.mapper.TbTeamRoleMapper;
import kr.city.eng.pendding.store.repo.TbTeamRoleRepo;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTeamRoleControllerTest {

  @Autowired
  TestStoreInitialize initialize;
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ApiTeamRoleController controller;
  @Autowired
  TbTeamRoleRepo store;
  @Autowired
  TbTeamRoleMapper storeMapper;

  private TbTeam teamEntity;
  private MockMvc mockMvc;
  private final MockTeamRoleService mockService = new MockTeamRoleService();

  @BeforeEach
  public void setUp() {
    initialize.clearAll();
    this.teamEntity = initialize.initTeam();

    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();

    mockService.setUp(objectMapper, mockMvc);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void create() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamRole dto = mockService.add(teamId, mockService.create());

    TbTeamRole result = store.findById(dto.getId()).get();
    assertEquals(dto, storeMapper.toDto(result));
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void getAll() throws Exception {
    Long teamId = this.teamEntity.getId();
    List<TeamRole> lists = this.teamEntity.getTeamRoles().stream()
        .map(storeMapper::toDto)
        .collect(Collectors.toList());
    for (int i = 0; i < 3; i++) {
      lists.add(mockService.add(teamId, mockService.create()));
    }

    List<TeamRole> result = mockService.getAll(teamId);
    assertEquals(lists.size(), result.size());

    List<Long> ids = lists.stream().map(it -> it.getId()).sorted().toList();
    List<Long> ids2 = result.stream().map(it -> it.getId()).sorted().toList();
    assertEquals(ids, ids2);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void getById() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamRole dto = mockService.add(teamId, mockService.create());

    TeamRole result = mockService.getById(dto.getId());
    assertEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void update() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamRole dto = mockService.add(teamId, mockService.create());

    TeamRole result = mockService.update(teamId, dto.getId(), dto);
    assertEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void delete() throws Exception {
    Long teamId = this.teamEntity.getId();
    TeamRole dto = mockService.add(teamId, mockService.create());

    mockService.delete(teamId, dto.getId());

    Optional<TbTeamRole> op = store.findById(dto.getId());
    assertFalse(op.isPresent());
  }

}
