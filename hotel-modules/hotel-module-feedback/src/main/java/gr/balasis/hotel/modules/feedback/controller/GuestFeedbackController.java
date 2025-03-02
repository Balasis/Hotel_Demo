package gr.balasis.hotel.modules.feedback.controller;

import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.modules.feedback.base.BaseComponent;
import gr.balasis.hotel.modules.feedback.domain.Feedback;
import gr.balasis.hotel.modules.feedback.entity.FeedbackEntity;
import gr.balasis.hotel.modules.feedback.mapper.FeedbackMapper;
import gr.balasis.hotel.modules.feedback.resource.FeedbackResource;
import gr.balasis.hotel.modules.feedback.service.BaseService;
import gr.balasis.hotel.modules.feedback.service.FeedbackServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("guests/{guestsId}/reservation/{reservationId}/feedback")
@RequiredArgsConstructor
public class GuestFeedbackController extends BaseComponent {
    private final FeedbackServiceImpl feedbackService;
    private final FeedbackMapper feedbackMapper;

    @PostMapping
    public ResponseEntity<FeedbackResource> submitFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId,
            @RequestBody FeedbackResource resource) {
        Feedback feedback = feedbackService.createFeedback(guestsId, reservationId, feedbackMapper.toDomainFromResource(resource));
        return ResponseEntity.ok(feedbackMapper.toResource(feedback));
    }

    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackResource> getFeedbackById(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId,
            @PathVariable Long feedbackId) {
        Feedback feedback = feedbackService.getFeedbackById(guestsId, reservationId, feedbackId);
        return ResponseEntity.ok(feedbackMapper.toResource(feedback));
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<FeedbackResource> updateFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId,
            @PathVariable Long feedbackId,
            @RequestBody FeedbackResource updatedResource) {
        Feedback updatedFeedback = feedbackService.updateFeedback(
                guestsId, reservationId, feedbackId, feedbackMapper.toDomainFromResource(updatedResource));
        return ResponseEntity.ok(feedbackMapper.toResource(updatedFeedback));
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId,
            @PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(guestsId, reservationId, feedbackId);
        return ResponseEntity.noContent().build();
    }
}
