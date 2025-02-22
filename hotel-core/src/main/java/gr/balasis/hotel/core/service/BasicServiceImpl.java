package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.BaseDomain;
import gr.balasis.hotel.core.base.BaseComponent;

import gr.balasis.hotel.core.entity.BaseEntity;
import gr.balasis.hotel.core.mapper.entitydomain.EDbaseMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicServiceImpl<T extends BaseDomain, E extends BaseEntity> extends BaseComponent implements BaseService<T,Long>{
    public abstract JpaRepository<E,Long> getRepository();
    public abstract EDbaseMapper<E,T> getMapper();

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
    public T findById(Long id) {
        E entity = getRepository().findById(id).orElseThrow();
        return getMapper().toDomain(entity);
    }

    @Override
    public List<T> findAll() {
        List<E> entities = getRepository().findAll();
        return entities.stream()
                .map(getMapper()::toDomain)
                .collect(Collectors.toList());
    }

}
