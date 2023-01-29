package kr.city.eng.pendding.controller.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;

import kr.city.eng.pendding.dto.TeamRole;
import kr.city.eng.pendding.dto.TeamRoleDto;
import kr.city.eng.pendding.store.entity.enums.TeamPermission;

public class MockTeamRoleService extends MockService {

  @Override
  public String getUrl(String url) {
    return "/api/teams" + url;
  }

  private TeamRole convertDto(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString();
    return getData(json, new TypeReference<TeamRole>() {
    });
  }

  private List<TeamRole> convertList(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString();
    return getData(json, new TypeReference<List<TeamRole>>() {
    });
  }

  public TeamRoleDto create() {
    TeamRoleDto dto = new TeamRoleDto();
    dto.setName("TeamRole-" + RandomStringUtils.randomAlphabetic(2));
    dto.setPermissions(TeamPermission.member());
    return dto;
  }

  public TeamRole add(Long teamId, TeamRoleDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPost("/{teamId}/role", teamId).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public List<TeamRole> getAll(Long teamId) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/{teamId}/roles", teamId))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();
    return convertList(result);
  }

  public TeamRole getById(Long id) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/roles/{id}", id))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public TeamRole update(Long id, TeamRoleDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPut("/roles/{id}", id).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public void delete(Long id) throws Exception {
    mockMvc.perform(apiDel("/roles/{id}", id))
        .andExpect(status().isNoContent())
        .andReturn();
  }

}
