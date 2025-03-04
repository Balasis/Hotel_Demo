package gr.balasis.hotel.context.base.service;

import gr.balasis.hotel.context.base.domain.BaseDomain;
import gr.balasis.hotel.context.base.component.BaseComponent;
import gr.balasis.hotel.context.base.entity.BaseEntity;
import gr.balasis.hotel.context.base.exception.EntityNotFoundException;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicServiceImpl<T extends BaseDomain, E extends BaseEntity> extends BaseComponent implements BaseService<T> {
    public abstract JpaRepository<E, Long> getRepository();
    public abstract BaseMapper<T,E> getMapper();

    @Override
    public T create(final T item) {
        E entity = getMapper().toEntity(item);
        E savedEntity = getRepository().save(entity);
        return getMapper().toDomain(savedEntity);
    }

    @Override
    public void update(T item) {
        E entity = getMapper().toEntity(item);
        getRepository().save(entity);
    }

    @Override
    public void delete(T item) {
        E entity = getMapper().toEntity(item);
        getRepository().delete(entity);
    }

    @Override
    public List<T> findAll() {
        List<E> entities = getRepository().findAll();
        return entities.stream()
                .map(getMapper()::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(final T item) {
        return getRepository().existsById(item.getId());
    }

}
