package com.simitchiyski.multitenant.core.abstraction.mapper;

import org.mapstruct.Mapping;
import org.springframework.data.domain.Persistable;

import java.util.List;

public interface DtoToEntityMapper<Dto, Entity extends Persistable<Long>> {

    @Mapping(ignore = true, target = "id")
    Entity toEntity(Dto dto);

    List<Entity> toEntity(List<Dto> dtoList);
}
