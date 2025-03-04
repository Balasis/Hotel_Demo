package gr.balasis.hotel.engine.core.mapper.web;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.mapper.BaseWebMapper;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true),
        uses = {RoomWebMapper.class, GuestWebMapper.class, PaymentWebMapper.class})
public interface ReservationWebMapper extends BaseWebMapper<Reservation, ReservationResource> {

}
