package gr.balasis.hotel.context.base.exception;

public class DuplicateFeedbackException extends DataConflictException {
    public DuplicateFeedbackException(String message) {
        super(message);
    }
}
