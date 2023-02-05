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
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;

import kr.city.eng.pendding.dto.TeamProduct;
import kr.city.eng.pendding.dto.TeamProductDto;

public class MockTeamProductService extends MockService {

  @Override
  public String getUrl(String url) {
    return "/api/teams" + url;
  }

  private TeamProduct convertDto(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString();
    return getData(json, new TypeReference<TeamProduct>() {
    });
  }

  private List<TeamProduct> convertPage(MvcResult result) throws Exception {
    String json = result.getResponse().getContentAsString();
    return getData(json, new TypeReference<List<TeamProduct>>() {
    }, "content");
  }

  public TeamProductDto create() {
    TeamProductDto dto = new TeamProductDto();
    dto.setName("TeamProductName-" + RandomStringUtils.randomAlphabetic(2));
    dto.setBarcode(RandomStringUtils.randomNumeric(10));
    return dto;
  }

  public TeamProduct add(Long teamId, TeamProductDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPost("/{teamId}/product", teamId).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public List<TeamProduct> getPage(Long teamId, LinkedMultiValueMap<String, String> params) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/{teamId}/product/page", teamId).params(params))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();
    return convertPage(result);
  }

  public TeamProduct getById(Long id) throws Exception {
    MvcResult result = mockMvc.perform(apiGet("/products/{id}", id))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public TeamProduct update(Long id, TeamProductDto dto) throws Exception {
    String content = mapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(apiPatch("/products/{id}", id).content(content))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(notNullValue())))
        .andReturn();
    return convertDto(result);
  }

  public void delete(Long id) throws Exception {
    mockMvc.perform(apiDel("/products/{id}", id))
        .andExpect(status().isNoContent())
        .andReturn();
  }

}
