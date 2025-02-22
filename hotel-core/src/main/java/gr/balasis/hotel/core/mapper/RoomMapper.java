package gr.balasis.hotel.core.mapper;

import gr.balasis.hotel.context.web.resource.RoomResource;
import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.core.entity.RoomEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toDomain(RoomResource roomResource );
    Room toDomain(RoomEntity roomResource );
    RoomResource toResource(Room room);
    RoomEntity toEntity(Room room);
}
