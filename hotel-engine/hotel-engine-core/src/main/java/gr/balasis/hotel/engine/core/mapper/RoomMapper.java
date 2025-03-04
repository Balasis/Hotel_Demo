package gr.balasis.hotel.engine.core.mapper;



@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface RoomMapper extends BaseMapper<Room,RoomEntity> {
}
