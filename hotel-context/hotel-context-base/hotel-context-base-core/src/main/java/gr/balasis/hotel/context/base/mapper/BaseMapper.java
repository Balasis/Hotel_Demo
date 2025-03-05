package gr.balasis.hotel.context.base.mapper;

import java.util.List;

public interface BaseMapper<D,E> {

    D toDomain(E entity);
    List<D> toDomains (List<E> entities);
    E toEntity(D domain);
    List<E>toEntities(List<D> domain);

}
