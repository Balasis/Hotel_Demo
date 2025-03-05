package gr.balasis.hotel.engine.core.mapper.base;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.base.entity.ReservationEntity;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true),
        uses = {RoomMapper.class, GuestMapper.class})
public interface ReservationMapper extends BaseMapper<Reservation, ReservationEntity> {

}
