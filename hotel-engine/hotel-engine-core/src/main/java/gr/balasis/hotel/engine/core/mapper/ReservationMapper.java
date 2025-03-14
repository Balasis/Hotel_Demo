package gr.balasis.hotel.engine.core.mapper;

import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {RoomMapper.class, GuestMapper.class, FeedbackMapper.class, PaymentMapper.class})
public interface ReservationMapper extends BaseMapper<Reservation, ReservationResource> {

}
