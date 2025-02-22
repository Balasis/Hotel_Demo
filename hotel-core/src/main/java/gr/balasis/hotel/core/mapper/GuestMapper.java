package gr.balasis.hotel.core.mapper;

import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.core.entity.GuestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    Guest toDomain(GuestResource guestResource );
    Guest toDomain(GuestEntity guestResource );
    GuestResource toResource(Guest guest);
    GuestEntity toEntity(Guest guest);
}
