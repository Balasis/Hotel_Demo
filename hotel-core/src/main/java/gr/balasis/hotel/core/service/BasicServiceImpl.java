package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.BaseDomain;
import gr.balasis.hotel.context.web.resource.BaseResource;
import gr.balasis.hotel.core.base.BaseComponent;
import gr.balasis.hotel.core.entity.BaseEntity;
import gr.balasis.hotel.core.exception.EntityNotFoundException;
import gr.balasis.hotel.core.mapper.BaseMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicServiceImpl<T extends BaseDomain,R extends BaseResource, E extends BaseEntity > extends BaseComponent implements BaseService<T,Long>{
    public abstract JpaRepository<E,Long> getRepository();
    public abstract BaseMapper<T,R,E> getMapper();

    @Override
    public T create(final T item) {
        E entity = getMapper().toEntity(item);
        E savedEntity = getRepository().save(entity);
        return getMapper().toDomainFromEntity(savedEntity);
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
        E entity = getRepository().findById(id).orElseThrow(
                () -> new EntityNotFoundException("Entity not found with id: " + id));
        return getMapper().toDomainFromEntity(entity);
    }

    @Override
    public List<T> findAll() {
        List<E> entities = getRepository().findAll();
        return entities.stream()
                .map(getMapper()::toDomainFromEntity)
                .collect(Collectors.toList());
    }

}
