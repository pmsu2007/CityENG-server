package kr.city.eng.pendding.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.city.eng.pendding.TestStoreInitialize;
import kr.city.eng.pendding.controller.mock.MockTeamService;
import kr.city.eng.pendding.controller.mock.MockUserService;
import kr.city.eng.pendding.dto.Team;
import kr.city.eng.pendding.dto.TeamInfo;
import kr.city.eng.pendding.dto.User;
import kr.city.eng.pendding.dto.UserDto;
import kr.city.eng.pendding.dto.UserPassword;
import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.mapper.TbUserMapper;
import kr.city.eng.pendding.store.repo.TbTeamRepo;
import kr.city.eng.pendding.store.repo.TbUserRepo;

@SpringBootTest
@ActiveProfiles("test")
public class ApiUserConterollerTest {

  @Autowired
  TestStoreInitialize initialize;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  BCryptPasswordEncoder passwordEncoder;
  @Autowired
  TbUserRepo store;
  @Autowired
  TbTeamRepo storeTeam;
  @Autowired
  TbUserMapper storeMapper;

  @Autowired
  ApiUserController controller;
  @Autowired
  ApiTeamController tamController;

  private MockMvc mockMvc;
  private final MockUserService mockService = new MockUserService();
  private final MockTeamService mockTeamService = new MockTeamService();

  @BeforeEach
  public void setUp() {
    initialize.clearAll();

    mockMvc = MockMvcBuilders.standaloneSetup(controller, tamController)
        .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();

    mockService.setUp(objectMapper, mockMvc);
    mockTeamService.setUp(objectMapper, mockMvc);
  }

  @Test
  public void create() throws Exception {
    User dto = mockService.add(mockService.create("test"));

    TbUser result = store.findById(dto.getId()).get();
    assertEquals(dto, storeMapper.toDto(result));
  }

  @Test
  @WithMockUser(username = "test", roles = { "USER" })
  public void getById() throws Exception {
    User dto = mockService.add(mockService.create("test"));

    User result = mockService.getById();
    assertEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "test", roles = { "USER" })
  public void update() throws Exception {
    UserDto dto = mockService.create("test");
    User user = mockService.add(dto);
    dto.setName("update name");
    user.setName(dto.getName());
    dto.setImageUrl("change_image_url.png");
    user.setImageUrl(dto.getImageUrl());

    User result = mockService.update(dto);
    assertEquals(user, result);
  }

  @Test
  @WithMockUser(username = "test", roles = { "USER" })
  public void delete() throws Exception {
    User dto = mockService.add(mockService.create("test"));
    Team teamDto = mockTeamService.add(mockTeamService.create());

    mockService.delete(dto.getId());

    Optional<TbTeam> opTeam = storeTeam.findById(teamDto.getId());
    assertFalse(opTeam.isPresent());
    Optional<TbUser> op = store.findById(dto.getId());
    assertFalse(op.isPresent());
  }

  @Test
  @WithMockUser(username = "test", roles = { "USER" })
  public void resetPassword() throws Exception {
    UserDto dto = mockService.create("test");
    mockService.add(dto);

    String newPw = RandomStringUtils.randomAlphabetic(8);
    UserPassword pw = new UserPassword(newPw, dto.getPassword());
    assertTrue(mockService.resetPassword(pw));

    TbUser user = store.findById(dto.getId()).get();
    assertTrue(passwordEncoder.matches(newPw, user.getPassword()));
  }

  @Test
  @WithMockUser(username = "test", roles = { "USER" })
  public void existById() throws Exception {
    User user = mockService.add(mockService.create("test"));
    assertTrue(mockService.existById(user.getId()));
  }

  @Test
  @WithMockUser(username = "test", roles = { "USER" })
  public void existByEmail() throws Exception {
    User user = mockService.add(mockService.create("test"));
    assertTrue(mockService.existByEmail(user.getEmail()));
  }

  @Test
  @WithMockUser(username = "test", roles = { "USER" })
  public void getTeamInfos() throws Exception {
    mockService.add(mockService.create("test"));
    Team teamDto = mockTeamService.add(mockTeamService.create());

    List<TeamInfo> result = mockService.getTeamInfos();
    assertEquals(result.size(), 1);
    TeamInfo teamInfo = result.get(0);
    assertEquals(teamDto.getId(), teamInfo.getId());
    assertEquals(teamDto.getName(), teamInfo.getName());
    assertEquals(teamDto.getMemo(), teamInfo.getMemo());
    assertEquals(teamDto.getImageUrl(), teamInfo.getImageUrl());
    assertEquals(0, teamInfo.getProductCount());
    assertEquals(0, teamInfo.getPlaceCount());
    assertEquals(0, teamInfo.getUserCount());
  }

}
