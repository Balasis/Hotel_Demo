package gr.balasis.hotel.core.mapper;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.data.entity.GuestEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface GuestMapper extends BaseMapper<Guest, GuestResource, GuestEntity> {

}
