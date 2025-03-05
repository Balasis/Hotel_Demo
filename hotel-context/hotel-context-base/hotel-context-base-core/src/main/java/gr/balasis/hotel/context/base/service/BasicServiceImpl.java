package gr.balasis.hotel.context.base.service;

import gr.balasis.hotel.context.base.domain.BaseDomain;
import gr.balasis.hotel.context.base.component.BaseComponent;
import gr.balasis.hotel.context.base.entity.BaseEntity;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class BasicServiceImpl<T extends BaseDomain, E extends BaseEntity> extends BaseComponent implements BaseService<T> {
    public abstract JpaRepository<E, Long> getRepository();
    public abstract BaseMapper<T,E> getMapper();

    @Override
    public T create(final T item) {
        return getMapper().toDomain(
                getRepository().save(
                        getMapper().toEntity(item))
        );
    }

    @Override
    public void update(T item) {
        getRepository().save(
                getMapper().toEntity(item));
    }

    @Override
    public void delete(T item) {
        getRepository().delete(
                getMapper().toEntity(item));
    }

    @Override
    public List<T> findAll() {
        return getMapper().toDomains(
                getRepository().findAll()) ;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(final T item) {
        return getRepository().existsById(item.getId());
    }

}
