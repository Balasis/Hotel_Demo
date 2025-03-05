package gr.balasis.hotel.engine.core.mapper.web;

import gr.balasis.hotel.context.base.domain.Feedback;
import gr.balasis.hotel.context.web.mapper.BaseWebMapper;
import gr.balasis.hotel.context.web.resource.FeedbackResource;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {ReservationWebMapper.class})
public interface FeedbackWebMapper extends BaseWebMapper<Feedback, FeedbackResource> {
}
