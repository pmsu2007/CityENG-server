package kr.city.eng.pendding.controller.mock;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;

import kr.city.eng.pendding.dto.TeamUser;

public class MockTeamUserService extends MockService {

  @Override
  public String getUrl(String url) {
    return "/api/teams/{teamId}" + url;
  }

  private TeamUser convertDto(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<TeamUser>() {
    });
  }

  public TeamUser add(Long teamId, TeamUser dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPost("/user", teamId).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andReturn();
    return convertDto(result);
  }

  public TeamUser update(Long id, TeamUser dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPut("/user", id).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();
    return convertDto(result);
  }

  public void delete(Long id, String userId) throws Exception {
    mockMvc.perform(apiDel("/users/{userId}", id, userId))
        .andExpect(status().isNoContent())
        .andReturn();
  }

}
