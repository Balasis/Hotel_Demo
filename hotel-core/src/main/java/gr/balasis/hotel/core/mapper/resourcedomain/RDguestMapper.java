package gr.balasis.hotel.core.mapper.resourcedomain;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.web.resource.GuestResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RDguestMapper extends RDbaseMapper<GuestResource,Guest> {
}
