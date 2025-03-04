package gr.balasis.hotel.engine.core.mapper;

import gr.balasis.hotel.context.base.domain.Payment;

import gr.balasis.hotel.context.base.entity.PaymentEntity;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PaymentMapper extends BaseMapper<Payment, PaymentEntity> {
}