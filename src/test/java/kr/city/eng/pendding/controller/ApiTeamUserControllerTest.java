package kr.city.eng.pendding.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import kr.city.eng.pendding.TestStoreInitialize;
import kr.city.eng.pendding.controller.mock.MockTeamUserService;
import kr.city.eng.pendding.controller.mock.MockUserService;
import kr.city.eng.pendding.dto.TeamUser;
import kr.city.eng.pendding.dto.User;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.repo.TbTeamUserRepo;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTeamUserControllerTest {

  @Autowired
  TestStoreInitialize initialize;
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ApiTeamUserController controller;
  @Autowired
  ApiUserController userController;
  @Autowired
  TbTeamUserRepo store;

  private TbTeam teamEntity;
  private Long teamAdminRoleId;
  private Long teamMemberRoleId;
  private MockMvc mockMvc;

  private final MockUserService mockUser = new MockUserService();
  private final MockTeamUserService mockService = new MockTeamUserService();

  @BeforeEach
  public void setUp() {
    initialize.clearAll();
    this.teamEntity = initialize.initTeam();
    this.teamAdminRoleId = this.teamEntity.getTeamRoles().get(1).getId();
    this.teamMemberRoleId = this.teamEntity.getTeamRoles().get(0).getId();

    mockMvc = MockMvcBuilders.standaloneSetup(userController, controller)
        .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();

    mockUser.setUp(objectMapper, mockMvc);
    mockService.setUp(objectMapper, mockMvc);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void create() throws Exception {
    User user = mockUser.add(mockUser.create("test"));
    Long teamId = this.teamEntity.getId();
    TeamUser teamUser = new TeamUser(user.getId(), teamMemberRoleId);

    TeamUser result = mockService.add(teamId, teamUser);
    assertEquals(teamUser, result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void update() throws Exception {
    User user = mockUser.add(mockUser.create("test"));
    Long teamId = this.teamEntity.getId();
    TeamUser teamUser = new TeamUser(user.getId(), teamMemberRoleId);

    TeamUser dto = mockService.add(teamId, teamUser);
    assertEquals(teamUser, dto);

    // 권한 변경
    teamUser = new TeamUser(user.getId(), teamAdminRoleId);
    TeamUser result = mockService.update(teamId, teamUser);
    assertEquals(teamUser, result);
  }

  @Test
  @WithMockUser(username = "admin", authorities = { "ADMIN" })
  public void delete() throws Exception {
    User user = mockUser.add(mockUser.create("test"));
    Long teamId = this.teamEntity.getId();
    TeamUser teamUser = new TeamUser(user.getId(), teamMemberRoleId);

    TeamUser dto = mockService.add(teamId, teamUser);
    assertEquals(teamUser, dto);

    mockService.delete(teamId, user.getId());
  }

}
