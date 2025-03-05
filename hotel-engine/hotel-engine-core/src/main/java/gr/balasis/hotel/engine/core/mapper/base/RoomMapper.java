package gr.balasis.hotel.engine.core.mapper.base;


import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.context.base.entity.RoomEntity;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface RoomMapper extends BaseMapper<Room, RoomEntity> {
}
