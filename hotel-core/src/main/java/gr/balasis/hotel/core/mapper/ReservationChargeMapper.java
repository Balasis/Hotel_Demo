package gr.balasis.hotel.core.mapper;

import gr.balasis.hotel.context.base.domain.ReservationCharge;
import gr.balasis.hotel.context.web.resource.ReservationChargeResource;
import gr.balasis.hotel.core.entity.ReservationChargeEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ReservationChargeMapper extends BaseMapper<ReservationCharge, ReservationChargeResource, ReservationChargeEntity>{

    @Override
    @Mapping(source = "reservation.id", target = "reservationId")
    ReservationCharge toDomainFromEntity(ReservationChargeEntity entity);

    @Override
    @Mapping(source = "reservationId", target = "reservation.id")
    ReservationChargeEntity toEntity(ReservationCharge domain);

    @Override
    @Mapping(source = "reservationId", target = "reservationId")
    ReservationChargeResource toResource(ReservationCharge domain);

    @Override
    @Mapping(source = "reservationId", target = "reservationId")
    ReservationCharge toDomainFromResource(ReservationChargeResource resource);
}
