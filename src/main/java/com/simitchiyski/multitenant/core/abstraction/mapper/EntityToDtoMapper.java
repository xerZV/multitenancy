package com.simitchiyski.multitenant.core.abstraction.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Persistable;

import java.util.List;

public interface EntityToDtoMapper<Entity extends Persistable<Long>, Dto> {

    Dto toDto(Entity entity);

    List<Dto> toDto(List<Entity> entityList);

    default Page<Dto> toDtoPage(Page<Entity> entityPage) {
        return new PageImpl<>(toDto(entityPage.getContent()), entityPage.getPageable(), entityPage.getTotalPages());
    }
}
