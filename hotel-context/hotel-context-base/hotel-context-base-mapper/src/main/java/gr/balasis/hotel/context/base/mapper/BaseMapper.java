package gr.balasis.hotel.context.base.mapper;

public interface BaseMapper<D, R, E> {

    D toDomainFromResource(R resource);

    D toDomainFromEntity(E entity);

    R toResource(D domain);

    E toEntity(D domain);
}
