package gr.balasis.hotel.core.mapper;

import gr.balasis.hotel.context.base.domain.ReservationCharge;
import gr.balasis.hotel.context.web.resource.ReservationChargeResource;
import gr.balasis.hotel.core.entity.ReservationChargeEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ReservationChargeMapper extends BaseMapper<ReservationCharge, ReservationChargeResource, ReservationChargeEntity>{
}
