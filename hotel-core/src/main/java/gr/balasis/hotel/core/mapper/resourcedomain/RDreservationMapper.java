package gr.balasis.hotel.core.mapper.resourcedomain;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RDreservationMapper extends RDbaseMapper<ReservationResource, Reservation>{
}
