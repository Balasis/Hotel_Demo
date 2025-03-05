package gr.balasis.hotel.context.base.exception;

public class DuplicateEmailException extends DataConflictException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
