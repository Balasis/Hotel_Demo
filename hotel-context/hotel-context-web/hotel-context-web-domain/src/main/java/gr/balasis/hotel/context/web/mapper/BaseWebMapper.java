package gr.balasis.hotel.context.web.mapper;

import java.util.List;

public interface BaseWebMapper<D,R> {

    D toDomain(R resource);
    List<D> toDomains(List<R> resources);
    R toResource(D domain);
    List<R> toResources(List<D> domains);
}