package kr.city.eng.pendding.controller.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;

import kr.city.eng.pendding.dto.TeamAttr;
import kr.city.eng.pendding.dto.TeamAttrDto;
import kr.city.eng.pendding.store.entity.enums.AttrType;

public class MockTeamAttrService extends MockService {

  @Override
  public String getUrl(String url) {
    return "/api/teams" + url;
  }

  private TeamAttr convertDto(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<TeamAttr>() {
    });
  }

  private List<TeamAttr> convertList(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<List<TeamAttr>>() {
    });
  }

  private List<TeamAttr> convertPage(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<List<TeamAttr>>() {
    }, "content");
  }

  public TeamAttrDto create(int index) {
    TeamAttrDto dto = new TeamAttrDto();
    dto.setIndex(index);
    // dto.setType(AttrType.NUMBER);
    dto.setName("TeamAttr:" + AttrType.NUMBER);
    return dto;
  }

  public TeamAttrDto createEnumAttr() {
    TeamAttrDto dto = new TeamAttrDto();
    dto.setIndex(0);
    dto.setType(AttrType.STRING);
    dto.setName("종류");
    dto.addValue("도시가스", "LPG", "기타");
    return dto;
  }

  public TeamAttr add(Long teamId, TeamAttrDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPost("/{teamId}/attr", teamId).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public TeamAttr update(Long teamId, Long id, TeamAttrDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPut("/{teamId}/attrs/{id}", teamId, id).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public void delete(Long teamId, Long id) throws Exception {
    mockMvc.perform(apiDel("/{teamId}/attrs/{id}", teamId, id))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  public List<TeamAttr> getAll(Long teamId) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/{teamId}/attrs", teamId))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();
    return convertList(result);
  }

  public List<TeamAttr> getPage(Long teamId, LinkedMultiValueMap<String, String> params) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/{teamId}/attrs/page", teamId).params(params))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();
    return convertPage(result);
  }

  public TeamAttr getById(Long id) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/attrs/{id}", id))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public List<String> getAttrValues(Long id) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/attrs/{id}/values", id))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();
    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    return getData(json, new TypeReference<List<String>>() {
    });
  }

}
