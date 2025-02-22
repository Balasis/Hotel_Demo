package gr.balasis.hotel.core.mapper.entitydomain;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.core.entity.GuestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EDguestMapper extends EDbaseMapper<GuestEntity,Guest> {

}
