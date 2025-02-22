package gr.balasis.hotel.core.mapper;

import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.core.entity.ReservationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    Reservation toDomain(ReservationResource reservationResource);
    Reservation toDomain(ReservationEntity reservationResource);
    ReservationResource toResource(Reservation reservation);
    ReservationEntity toEntity(Reservation reservation);
}
