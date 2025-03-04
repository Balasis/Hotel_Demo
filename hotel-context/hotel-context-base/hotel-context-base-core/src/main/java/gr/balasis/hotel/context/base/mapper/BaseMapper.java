package gr.balasis.hotel.context.base.mapper;

public interface BaseMapper<D,E> {

    D toDomain(E entity);

    E toEntity(D domain);
}
