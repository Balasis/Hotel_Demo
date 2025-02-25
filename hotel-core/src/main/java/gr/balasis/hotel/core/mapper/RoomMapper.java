package gr.balasis.hotel.core.mapper;

import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.context.web.resource.RoomResource;
import gr.balasis.hotel.core.entity.RoomEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface RoomMapper extends BaseMapper<Room, RoomResource, RoomEntity> {
}
