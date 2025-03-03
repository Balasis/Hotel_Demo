package gr.balasis.hotel.modules.feedback.controller;

import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.modules.feedback.base.BaseComponent;
import gr.balasis.hotel.modules.feedback.domain.Feedback;
import gr.balasis.hotel.modules.feedback.entity.FeedbackEntity;
import gr.balasis.hotel.modules.feedback.mapper.FeedbackMapper;
import gr.balasis.hotel.modules.feedback.resource.FeedbackResource;
import gr.balasis.hotel.modules.feedback.service.BaseService;
import gr.balasis.hotel.modules.feedback.service.FeedbackServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("guests/{guestsId}/reservations/{reservationId}/feedback")
@RequiredArgsConstructor
public class GuestFeedbackController extends BaseComponent {
    private final FeedbackServiceImpl feedbackService;
    private final FeedbackMapper feedbackMapper;

    @PostMapping
    public ResponseEntity<FeedbackResource> submitFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId,
            @RequestBody @Valid FeedbackResource resource) {
        Feedback feedback = feedbackService.createFeedback(guestsId, reservationId, feedbackMapper.toDomainFromResource(resource));
        return ResponseEntity.ok(feedbackMapper.toResource(feedback));
    }

    @PutMapping
    public ResponseEntity<Void> updateFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId,
            @RequestBody FeedbackResource updatedResource) {
        feedbackService.updateFeedback(guestsId, reservationId,feedbackMapper.toDomainFromResource(updatedResource));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId) {
        feedbackService.deleteFeedback(guestsId, reservationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<FeedbackResource> getFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId) {
        Feedback feedback = feedbackService.getFeedbackById(guestsId, reservationId);
        return ResponseEntity.ok(feedbackMapper.toResource(feedback));
    }
}
