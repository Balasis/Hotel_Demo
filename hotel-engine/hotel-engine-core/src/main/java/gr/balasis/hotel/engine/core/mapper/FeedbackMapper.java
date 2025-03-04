package gr.balasis.hotel.engine.core.mapper;

import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.domain.Feedback;
import gr.balasis.hotel.context.base.entity.FeedbackEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {GuestMapper.class})
public interface FeedbackMapper extends BaseMapper<Feedback,FeedbackEntity> {
}
