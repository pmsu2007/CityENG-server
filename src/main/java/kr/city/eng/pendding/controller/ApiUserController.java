package kr.city.eng.pendding.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.city.eng.pendding.dto.TeamInfo;
import kr.city.eng.pendding.dto.User;
import kr.city.eng.pendding.dto.UserDto;
import kr.city.eng.pendding.dto.UserPassword;
import kr.city.eng.pendding.dto.UserSign;
import kr.city.eng.pendding.service.ApiUserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiUserController {

  private final ApiUserService service;

  private Object responseResult(Object result) {
    return Collections.singletonMap("result", result);
  }

  @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
  public String signIn(@RequestBody UserSign dto) {
    return service.signIn(dto);
  }

  @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<User> getUsers() {
    return service.getEntities();
  }

  @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
  public User getUser() {
    return service.getOrThrow();
  }

  @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.CREATED)
  public User createUser(@RequestBody UserDto dto) {
    dto.validate();
    return service.createOrThrow(dto);
  }

  @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
  public User updateUser(@RequestBody UserDto dto) {
    dto.validateUpdate();
    // 현재 접속한 유저정보로 확인
    return service.updateOrThrow(dto);
  }

  @DeleteMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteUser() {
    // 현재 접속한 유저정보로 확인
    service.deleteOrThrow();
  }

  @PutMapping(value = "/user/password", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void resetPassword(@RequestBody UserPassword model) {
    model.validate();
    // 현재 접속한 유저정보로 확인
    service.resetPassword(model);
  }

  @GetMapping(value = "/user/exist-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Object existById(@PathVariable String id) {
    return responseResult(service.existId(id));
  }

  @GetMapping(value = "/user/exist-email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Object existByEmail(@PathVariable String email) {
    return responseResult(service.existEmail(email));
  }

  @GetMapping(value = "/user/teams", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<TeamInfo> getTeamInfos() {
    return service.getTeams();
  }

}
