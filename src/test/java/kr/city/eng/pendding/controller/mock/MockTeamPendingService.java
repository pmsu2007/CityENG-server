package kr.city.eng.pendding.controller.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;

import kr.city.eng.pendding.dto.TeamPending;

public class MockTeamPendingService extends MockService {

  @Override
  public String getUrl(String url) {
    return "/api/teams" + url;
  }

  private TeamPending convertDto(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString();
    return getData(json, new TypeReference<TeamPending>() {
    });
  }

  public TeamPending pending(Long teamId, TeamPending dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPost("/{teamId}/pending", teamId).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public void delete(Long teamId, Long id) throws Exception {
    mockMvc.perform(apiDel("/{teamId}/pendings/{id}", teamId, id))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  public TeamPending getById(Long id) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/pendings/{id}", id))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

}
