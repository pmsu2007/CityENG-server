package kr.city.eng.pendding.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import kr.city.eng.pendding.dto.TeamInfo;
import kr.city.eng.pendding.dto.User;
import kr.city.eng.pendding.dto.UserDto;
import kr.city.eng.pendding.dto.UserPassword;
import kr.city.eng.pendding.store.entity.TbUser;
import kr.city.eng.pendding.store.mapper.TbTeamMapper;
import kr.city.eng.pendding.store.mapper.TbUserMapper;
import kr.city.eng.pendding.store.repo.TbUserRepo;
import kr.city.eng.pendding.util.AppUtil;
import kr.city.eng.pendding.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiUserService {

  private final BCryptPasswordEncoder passwordEncoder;
  private final TbUserRepo store;

  private final TbUserMapper mapper;
  private final TbTeamMapper mapperTeam;

  private TbUser findUserOrThrow(String userId) {
    return store.findById(userId)
        .orElseThrow(() -> ExceptionUtil.id(userId, TbUser.class.getName()));
  }

  @Transactional
  public User getOrThrow() {
    TbUser user = findUserOrThrow(AppUtil.getAuthUser());
    return mapper.toDto(user);
  }

  @Transactional
  public User createOrThrow(UserDto dto) {
    TbUser entity = mapper.toEntity(dto);
    entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    return mapper.toDto(store.save(entity));
  }

  @Transactional
  public User updateOrThrow(UserDto dto) {
    TbUser entity = findUserOrThrow(AppUtil.getAuthUser());
    mapper.updateEntity(entity, dto);
    return mapper.toDto(store.save(entity));
  }

  @Transactional
  public void deleteOrThrow() {
    TbUser entity = findUserOrThrow(AppUtil.getAuthUser());
    store.delete(entity);
  }

  @Transactional
  public void resetPassword(UserPassword dto) {
    TbUser entity = findUserOrThrow(AppUtil.getAuthUser());
    // 기존 패스워드가 맞는지 판단.
    if (!passwordEncoder.matches(dto.getOldPassword(), entity.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password");
    }
    // 성공시 새로운 패스워드로 입력
    String password = passwordEncoder.encode(dto.getNewPassword());
    entity.setPassword(password);
    entity.setUpdatedAt(System.currentTimeMillis());
    store.save(entity);
  }

  @Transactional
  public boolean existId(String id) {
    return store.existsById(id);
  }

  @Transactional
  public boolean existEmail(String email) {
    return store.existsByEmail(email);
  }

  @Transactional
  public List<TeamInfo> getTeams() {
    TbUser entity = findUserOrThrow(AppUtil.getAuthUser());
    return entity.getTeams().stream()
        .map(mapperTeam::toTeamInfo)
        .collect(Collectors.toList());
  }

}
