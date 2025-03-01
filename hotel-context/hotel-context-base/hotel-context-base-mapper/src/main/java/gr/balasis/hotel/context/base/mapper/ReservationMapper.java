package gr.balasis.hotel.context.base.mapper;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.data.entity.ReservationEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {RoomMapper.class,GuestMapper.class})
public interface ReservationMapper extends BaseMapper<Reservation, ReservationResource, ReservationEntity> {

}
