package gr.balasis.hotel.core.mapper.entitydomain;


import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.core.entity.RoomEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EDroomMapper extends EDbaseMapper<RoomEntity,Room> {

}
