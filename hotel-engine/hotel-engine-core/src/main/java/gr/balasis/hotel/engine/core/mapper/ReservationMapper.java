package gr.balasis.hotel.engine.core.mapper;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.data.entity.ReservationEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true),
        uses = {RoomMapper.class, GuestMapper.class, PaymentMapper.class})
public interface ReservationMapper extends BaseMapper<Reservation,ReservationEntity> {

}
