package gr.balasis.hotel.context.web.exception;

public class ReservationGuestIdMismatchException extends RuntimeException{
    public ReservationGuestIdMismatchException(String message){
        super(message);
    }
}
