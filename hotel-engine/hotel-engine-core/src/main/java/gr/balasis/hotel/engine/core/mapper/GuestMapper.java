package gr.balasis.hotel.engine.core.mapper;


import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.web.resource.GuestResource;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface GuestMapper extends BaseMapper<Guest, GuestResource> {

}
