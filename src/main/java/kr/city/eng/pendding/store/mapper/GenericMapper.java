package kr.city.eng.pendding.store.mapper;

import kr.city.eng.pendding.dto.AppDto;
import kr.city.eng.pendding.store.entity.TbEntity;

public interface GenericMapper<D extends AppDto, E extends TbEntity> {
  D toDto(E entity);

  E toEntity(D dto);
}
