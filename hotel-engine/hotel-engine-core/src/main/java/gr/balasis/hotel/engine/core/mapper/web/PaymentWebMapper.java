package gr.balasis.hotel.engine.core.mapper.web;

import gr.balasis.hotel.context.base.domain.Payment;
import gr.balasis.hotel.context.web.mapper.BaseWebMapper;
import gr.balasis.hotel.context.web.resource.PaymentResource;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true),uses = {ReservationWebMapper.class})
public interface PaymentWebMapper extends BaseWebMapper<Payment, PaymentResource> {
}