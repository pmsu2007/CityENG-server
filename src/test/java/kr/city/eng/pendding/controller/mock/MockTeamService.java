package kr.city.eng.pendding.controller.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;

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

  private Team convertDto(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<Team>() {
    });
  }

  private List<Team> convertList(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<List<Team>>() {
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

    return convertDto(result);
  }

  public List<Team> getAll() throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/teams"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();
    return convertList(result);
  }

  public Team getById(Long id) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/teams/{id}", id))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();

    return convertDto(result);
  }

  public Team update(Long id, TeamDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPut("/teams/{id}", id).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();

    return convertDto(result);
  }

  public void delete(Long id) throws Exception {
    mockMvc.perform(apiDel("/teams/{id}", id))
        .andExpect(status().isNoContent())
        .andReturn();
  }

}
