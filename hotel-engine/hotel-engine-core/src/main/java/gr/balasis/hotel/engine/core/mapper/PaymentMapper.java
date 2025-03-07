package gr.balasis.hotel.engine.core.mapper;



import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.web.resource.PaymentResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {ReservationMapper.class})
public interface PaymentMapper extends BaseMapper<Payment, PaymentResource> {
}