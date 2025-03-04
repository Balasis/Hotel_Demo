package gr.balasis.hotel.context.web.mapper;

import gr.balasis.hotel.context.base.domain.Feedback;
import gr.balasis.hotel.context.web.resource.FeedbackResource;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {GuestMapper.class})
public interface FeedbackMapper extends BaseMapper<Feedback, FeedbackResource> {
}
