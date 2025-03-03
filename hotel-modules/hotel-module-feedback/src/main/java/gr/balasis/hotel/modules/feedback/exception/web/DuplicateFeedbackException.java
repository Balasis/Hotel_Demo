package gr.balasis.hotel.modules.feedback.exception.web;

public class DuplicateFeedbackException extends RuntimeException {
    public DuplicateFeedbackException(String message) {
        super(message);
    }
}
