package gr.balasis.hotel.core.mapper.resourcedomain;

public interface RDbaseMapper<R,D> {
    D toDomain(R resource);
    R toResource(D domain);
}
