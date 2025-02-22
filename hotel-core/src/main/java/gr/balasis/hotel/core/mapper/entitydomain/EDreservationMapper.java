package gr.balasis.hotel.core.mapper.entitydomain;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.core.entity.ReservationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EDreservationMapper extends EDbaseMapper<ReservationEntity,Reservation > {

}
