package gr.balasis.hotel.engine.core.mapper;


import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.web.resource.FeedbackResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeedbackMapper extends BaseMapper<Feedback, FeedbackResource> {
}
