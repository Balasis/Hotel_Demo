package gr.balasis.hotel.modules.feedback.mapper;

import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.modules.feedback.domain.Feedback;
import gr.balasis.hotel.modules.feedback.entity.FeedbackEntity;
import gr.balasis.hotel.modules.feedback.resource.FeedbackResource;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface FeedbackMapper extends BaseMapper<Feedback, FeedbackResource, FeedbackEntity> {
}
