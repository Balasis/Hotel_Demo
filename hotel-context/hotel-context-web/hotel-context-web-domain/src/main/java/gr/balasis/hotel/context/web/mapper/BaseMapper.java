package gr.balasis.hotel.context.web.mapper;

public interface BaseMapper<D,R> {

    D toDomain(R resource);
    R toResource(D domain);
}