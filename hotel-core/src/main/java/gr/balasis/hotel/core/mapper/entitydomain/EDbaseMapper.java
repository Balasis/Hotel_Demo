package gr.balasis.hotel.core.mapper.entitydomain;

public interface EDbaseMapper<E, D>{
    D toDomain(E entity);
    E toEntity(D domain);
}
