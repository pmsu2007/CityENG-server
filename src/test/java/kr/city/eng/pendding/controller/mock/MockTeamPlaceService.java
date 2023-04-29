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
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;

import kr.city.eng.pendding.dto.TeamPlace;
import kr.city.eng.pendding.dto.TeamPlaceDto;

public class MockTeamPlaceService extends MockService {

  @Override
  public String getUrl(String url) {
    return "/api/teams" + url;
  }

  private TeamPlace convertDto(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<TeamPlace>() {
    });
  }

  private List<TeamPlace> convertList(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<List<TeamPlace>>() {
    });
  }

  private List<TeamPlace> convertPage(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<List<TeamPlace>>() {
    }, "content");
  }

  public TeamPlaceDto create() {
    TeamPlaceDto dto = new TeamPlaceDto();
    dto.setName("TeamPlace-" + RandomStringUtils.randomAlphabetic(2));
    dto.setMemo("TeamPlace-Memo-" + RandomStringUtils.randomAlphabetic(10));
    return dto;
  }

  public TeamPlace add(Long teamId, TeamPlaceDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPost("/{teamId}/place", teamId).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public TeamPlace update(Long teamId, Long id, TeamPlaceDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPut("/{teamId}/places/{id}", teamId, id).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public void delete(Long teamId, Long id) throws Exception {
    mockMvc.perform(apiDel("/{teamId}/places/{id}", teamId, id))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  public List<TeamPlace> getAll(Long teamId) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/{teamId}/places", teamId))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();
    return convertList(result);
  }

  public List<TeamPlace> getPage(Long teamId, LinkedMultiValueMap<String, String> params) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/{teamId}/places/page", teamId).params(params))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();
    return convertPage(result);
  }

  public TeamPlace getById(Long id) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/places/{id}", id))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

}
