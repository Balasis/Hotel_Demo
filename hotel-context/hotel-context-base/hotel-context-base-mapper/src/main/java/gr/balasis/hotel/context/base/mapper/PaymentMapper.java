package gr.balasis.hotel.context.base.mapper;

import gr.balasis.hotel.context.base.domain.Payment;
import gr.balasis.hotel.context.web.resource.PaymentResource;
import gr.balasis.hotel.data.entity.PaymentEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PaymentMapper extends BaseMapper<Payment, PaymentResource, PaymentEntity> {
}