package kr.city.eng.pendding.controller.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;

import kr.city.eng.pendding.dto.Team;
import kr.city.eng.pendding.dto.TeamDto;

public class MockTeamService extends MockService {

  @Override
  public String getUrl(String url) {
    return "/api" + url;
  }

  private Team convertTeam(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString();
    return getData(json, new TypeReference<Team>() {
    });
  }

  public TeamDto create() {
    TeamDto dto = new TeamDto();
    dto.setName("Team-" + RandomStringUtils.randomAlphabetic(2));
    return dto;
  }

  public Team add(TeamDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPost("/team").content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();

    return convertTeam(result);
  }

  public Team getById(Long id) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/teams/{id}", id))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();

    return convertTeam(result);
  }

  public Team update(Long id, TeamDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPut("/teams/{id}", id).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();

    return convertTeam(result);
  }

  public void delete(Long id) throws Exception {
    mockMvc.perform(apiDel("/teams/{id}", id))
        .andExpect(status().isNoContent())
        .andReturn();
  }

}
