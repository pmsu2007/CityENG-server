package kr.city.eng.pendding.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

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
import kr.city.eng.pendding.controller.mock.MockTeamService;
import kr.city.eng.pendding.dto.Team;
import kr.city.eng.pendding.store.entity.team.TbTeam;
import kr.city.eng.pendding.store.mapper.TbTeamMapper;
import kr.city.eng.pendding.store.repo.TbTeamRepo;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTeamControllerTest {

  @Autowired
  TestStoreInitialize initialize;
  @Autowired
  ObjectMapper mapper;

  @Autowired
  ApiTeamController controller;
  @Autowired
  TbTeamRepo store;
  @Autowired
  TbTeamMapper storeMapper;

  private MockMvc mockMvc;
  private final MockTeamService mockService = new MockTeamService();

  @BeforeEach
  public void setUp() {
    initialize.clearAll();
    initialize.initAdminUser();

    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper)).build();

    mockService.setUp(mapper, mockMvc);
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void create() throws Exception {
    Team dto = mockService.add(mockService.create());

    TbTeam result = store.findById(dto.getId()).get();
    assertEquals(dto, storeMapper.toDto(result));
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void getById() throws Exception {
    Team dto = mockService.add(mockService.create());

    Team result = mockService.getById(dto.getId());
    assertEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void update() throws Exception {
    Team dto = mockService.add(mockService.create());

    Team result = mockService.update(dto.getId(), dto);
    assertEquals(dto, result);
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  public void delete() throws Exception {
    Team dto = mockService.add(mockService.create());

    mockService.delete(dto.getId());

    Optional<TbTeam> op = store.findById(dto.getId());
    assertFalse(op.isPresent());
  }

}
