package gr.balasis.hotel.engine.core.mapper;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.entity.GuestEntity;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface GuestMapper extends BaseMapper<Guest, GuestEntity> {

}
