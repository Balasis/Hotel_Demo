package gr.balasis.hotel.context.base.exception.notfound;

public class PaymentNotFoundException extends EntityNotFoundException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
