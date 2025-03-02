package gr.balasis.hotel.modules.feedback.service;

import gr.balasis.hotel.context.web.exception.UnauthorizedAccessException;
import gr.balasis.hotel.modules.feedback.domain.Feedback;
import gr.balasis.hotel.modules.feedback.entity.FeedbackEntity;
import gr.balasis.hotel.modules.feedback.mapper.FeedbackMapper;
import gr.balasis.hotel.modules.feedback.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    public Feedback createFeedback(Long id,Feedback feedback) {
//        validateFeedbackOwnership(id);
        FeedbackEntity feedbackEntity = feedbackMapper.toEntity(feedback);
        FeedbackEntity savedEntity = feedbackRepository.save(feedbackEntity);
        return feedbackMapper.toDomainFromEntity(savedEntity);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll()
                .stream()
                .map(feedbackMapper::toDomainFromEntity)
                .toList();
    }

    private void validateFeedbackOwnership(Long guestId, FeedbackEntity feedbackEntity) {
        if (!feedbackEntity.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedAccessException("This feedback does not belong to the guest");
        }
    }
}
