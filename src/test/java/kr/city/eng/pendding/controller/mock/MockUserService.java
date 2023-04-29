package kr.city.eng.pendding.controller.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;

import kr.city.eng.pendding.dto.TeamInfo;
import kr.city.eng.pendding.dto.User;
import kr.city.eng.pendding.dto.UserDto;
import kr.city.eng.pendding.dto.UserPassword;
import kr.city.eng.pendding.dto.UserSign;
import kr.city.eng.pendding.store.entity.enums.AuthType;

public class MockUserService extends MockService {

  @Override
  public String getUrl(String url) {
    return "/api/user" + url;
  }

  private User convertUser(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<User>() {
    });
  }

  public String signIn(UserSign user) throws Exception {
    String content = mapper.writeValueAsString(user);

    String json = mockMvc.perform(
        MockMvcRequestBuilders.post("/api/signin")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE).content(content))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<Map<String, String>>() {
    }).get("apikey");
  }

  public UserDto create(String userId) {
    UserDto dto = new UserDto();
    dto.setId(userId);
    dto.setPassword(RandomStringUtils.randomAlphabetic(8));
    dto.setAuthentication(AuthType.SIGNIN);
    dto.setName("Name-" + RandomStringUtils.randomAlphabetic(2));
    dto.setEmail(RandomStringUtils.randomAlphabetic(4) + "@yourcompany.com");
    return dto;
  }

  public User add(UserDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPost("").content(content))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    return convertUser(result);
  }

  public List<User> getAll() throws Exception {
    MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.get("/api/users")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<List<User>>() {
    });
  }

  public User getById() throws Exception {
    MvcResult result = mockMvc.perform(apiGet(""))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    return convertUser(result);
  }

  public User update(UserDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPut("").content(content))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    return convertUser(result);
  }

  public void delete() throws Exception {
    mockMvc.perform(apiDel(""))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  public boolean resetPassword(UserPassword dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    mockMvc.perform(apiPut("/password").content(content))
        .andExpect(status().isNoContent())
        .andReturn();
    return true;
  }

  public boolean existById(String id) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/exist-id/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.result", is(notNullValue())))
        .andReturn();

    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<Map<String, Boolean>>() {
    }).get("result");
  }

  public boolean existByEmail(String email) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/exist-email/{email}", email))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.result", is(notNullValue())))
        .andReturn();

    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<Map<String, Boolean>>() {
    }).get("result");
  }

  public List<TeamInfo> getTeamInfos() throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/teams"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<List<TeamInfo>>() {
    });
  }

}
