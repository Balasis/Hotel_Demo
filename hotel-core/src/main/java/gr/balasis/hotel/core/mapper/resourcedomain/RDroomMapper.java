package gr.balasis.hotel.core.mapper.resourcedomain;

import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.context.web.resource.RoomResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RDroomMapper extends RDbaseMapper<RoomResource, Room> {
}
