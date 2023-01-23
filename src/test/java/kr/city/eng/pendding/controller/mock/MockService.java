package kr.city.eng.pendding.controller.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class MockService {

  protected ObjectMapper mapper;
  protected MockMvc mockMvc;

  public void setUp(ObjectMapper mapper, MockMvc mockMvc) {
    this.mapper = mapper;
    this.mockMvc = mockMvc;
  }

  public abstract String getUrl(String url);

  protected MockHttpServletRequestBuilder apiGet(String url, Object... parameter) {
    return MockMvcRequestBuilders.get(getUrl(url), parameter)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE);
  }

  protected MockHttpServletRequestBuilder apiPost(String url, Object... parameter) {
    return MockMvcRequestBuilders.post(getUrl(url), parameter)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE);
  }

  protected MockHttpServletRequestBuilder apiPut(String url, Object... parameter) {
    return MockMvcRequestBuilders.put(getUrl(url), parameter)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE);
  }

  protected MockHttpServletRequestBuilder apiDel(String url, Object... parameter) {
    return MockMvcRequestBuilders.delete(getUrl(url), parameter)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE);
  }

  protected <T> T getData(String json, Class<T> toValueTypeRef)
      throws JsonParseException, JsonMappingException, IOException, Exception {
    return mapper.readValue(json, toValueTypeRef);
  }

  protected <T> T getData(String json, Class<T> toValueTypeRef, String columnName)
      throws JsonParseException, JsonMappingException, IOException, Exception {
    Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
    });
    return mapper.convertValue(map.get(columnName), toValueTypeRef);
  }

  protected <T> T getData(String json, TypeReference<T> toValueTypeRef, String columnName)
      throws JsonParseException, JsonMappingException, IOException, Exception {
    Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
    });
    return mapper.convertValue(map.get(columnName), toValueTypeRef);
  }

  protected <T> T getData(String json, TypeReference<T> toValueTypeRef)
      throws JsonParseException, JsonMappingException, IOException, Exception {
    Type type = toValueTypeRef.getType();
    Class<?> classOfCollection = null;
    if (type != null) {
      if (type instanceof Class) {
        classOfCollection = (Class<?>) type;
      } else if (type instanceof ParameterizedType) { // supports generic of generic
        Type rawType = ((ParameterizedType) type).getRawType();
        classOfCollection = (Class<?>) rawType;
      }
    }

    if (classOfCollection == List.class) {
      List<Map<String, Object>> list = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
      });
      return mapper.convertValue(list, toValueTypeRef);
    }

    Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
    });
    return mapper.convertValue(map, toValueTypeRef);

  }

  protected MockMultipartFile getMockFile(String fileName) throws Exception {
    File file = new File(fileName);
    String orgFileName = file.getName();
    return new MockMultipartFile("files", orgFileName, null, new FileInputStream(file));
  }

  protected MockMultipartFile getMockProperties(String content) throws Exception {
    return new MockMultipartFile("properties", "", "application/json", content.getBytes());
  }

  public MultiValueMap<String, String> convertMapToMultiValueMap(Map<String, Object> params) {
    MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
    params.keySet().forEach(key -> {
      result.add(key, String.valueOf(params.get(key)));
    });
    return result;
  }

}
