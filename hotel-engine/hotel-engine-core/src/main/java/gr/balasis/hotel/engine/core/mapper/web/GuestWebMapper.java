package gr.balasis.hotel.engine.core.mapper.web;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.web.mapper.BaseWebMapper;
import gr.balasis.hotel.context.web.resource.GuestResource;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface GuestWebMapper extends BaseWebMapper<Guest, GuestResource> {

}
