package gr.balasis.hotel.engine.core.mapper;

import gr.balasis.hotel.context.base.model.Room;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.web.resource.RoomResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper extends BaseMapper<Room, RoomResource> {
}
