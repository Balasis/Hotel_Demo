package gr.balasis.hotel.core.mapper;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.core.entity.ReservationEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {GuestMapper.class, RoomMapper.class})
public interface ReservationMapper extends BaseMapper<Reservation, ReservationResource, ReservationEntity> {

}
