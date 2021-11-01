package com.simitchiyski.multitenant.core.abstraction.mapper;

import org.springframework.data.domain.Persistable;

public interface TwoWayMapper<Dto, Entity extends Persistable<Long>> extends EntityToDtoMapper<Entity, Dto>,
        DtoToEntityMapper<Dto, Entity> {
}
