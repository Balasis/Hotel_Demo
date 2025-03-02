package gr.balasis.hotel.modules.feedback.controller;

import gr.balasis.hotel.modules.feedback.domain.Feedback;
import gr.balasis.hotel.modules.feedback.mapper.FeedbackMapper;
import gr.balasis.hotel.modules.feedback.resource.FeedbackResource;
import gr.balasis.hotel.modules.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("guests/{id}/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;

    @PostMapping
    public ResponseEntity<FeedbackResource> submitFeedback(
            @PathVariable Long id,
            @RequestBody FeedbackResource feedbackResource) {
        Feedback feedback = feedbackService.createFeedback(id,feedbackMapper.toDomainFromResource(feedbackResource) );
        return ResponseEntity.ok(feedbackMapper.toResource(feedback));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResource>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback().stream()
                .map(feedbackMapper::toResource)
                .toList());
    }
}
