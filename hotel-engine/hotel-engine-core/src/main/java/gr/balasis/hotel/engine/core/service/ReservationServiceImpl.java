package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.enumeration.ReservationAction;
import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.base.exception.dublicate.DublicateException;
import gr.balasis.hotel.context.base.exception.notfound.FeedbackNotFoundException;
import gr.balasis.hotel.context.base.exception.notfound.PaymentNotFoundException;
import gr.balasis.hotel.context.base.exception.notfound.ReservationNotFoundException;
import gr.balasis.hotel.context.base.exception.notfound.RoomNotFoundException;
import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.context.base.enumeration.PaymentStatus;

import gr.balasis.hotel.context.base.service.BasicServiceImpl;
import gr.balasis.hotel.engine.core.repository.ReservationRepository;

import gr.balasis.hotel.engine.core.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl extends BasicServiceImpl<Reservation, ReservationNotFoundException> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public Reservation get(final Long reservationId) {

        return reservationRepository.findReservationByIdCompleteFetch(reservationId)
                .orElseThrow(
                        () -> new ReservationNotFoundException("No reservation found for ID: "
                                + reservationId));
    }

    @Override
    @Transactional(readOnly = true)
    public Feedback getFeedback(final Long reservationId) {

        return reservationRepository.getFeedbackByReservationId(reservationId)
                .orElseThrow(
                        () -> new FeedbackNotFoundException("No feedback found for reservation with ID: "
                                + reservationId));
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPayment(final  Long reservationId) {
        return reservationRepository.getPaymentByReservationId(reservationId)
                .orElseThrow(
                        () -> new PaymentNotFoundException("No payment found for reservation with ID: "
                                + reservationId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByGuestId(final Long guestId) {
        return reservationRepository.findReservationByGuestIdCompleteFetch(guestId);
    }

    @Override
    public Reservation create(final Reservation reservation) {
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.ACTIVE);
        var savedReservation = reservationRepository.save(reservation);
        savedReservation.setPayment(generatePayment(savedReservation,false));
        return reservationRepository.findReservationByIdCompleteFetch(savedReservation.getId()).orElseThrow(
                () -> new ReservationNotFoundException("Failed to fetch the reservation with" +
                        " associated guest and room details after saving."));
    }

    @Override
    public Feedback createFeedback(final Long reservationId,final Feedback feedback) {
       return createFeedbackProcess(reservationId, feedback);
    }

    @Override
    public void updateFeedback(final Long reservationId,final Feedback feedback) {
        createFeedbackProcess(reservationId, feedback);
    }

    @Override
    public void update(final Reservation reservation) {
        reservation.setPayment(generatePayment(reservation,true));
        reservation.setStatus(ReservationStatus.ACTIVE);
        if (reservation.getFeedback() != null){
            reservation.getFeedback().setReservation(reservation);
            reservation.getFeedback().setCreatedAt(LocalDateTime.now());
        }
        reservationRepository.save(reservation);
    }

    @Override
    public void manageReservationAction(final Long reservationId, String action) {
        if(action.equalsIgnoreCase(ReservationAction.CANCEL.toString()) ){
            cancelReservation(reservationId);
        }
        if(action.equalsIgnoreCase(ReservationAction.PAY.toString()) ){
            payReservation(reservationId);
        }
    }

    @Override
    public void deleteFeedback(final Long reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        if (reservation.getFeedback() == null){
            throw new FeedbackNotFoundException("Feedback doesn't exist to be deleted");
        }
        reservation.setFeedback(null);
        reservationRepository.save(reservation);
    }

    @Override
    public JpaRepository<Reservation, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public Class<ReservationNotFoundException> getNotFoundExceptionClass() {
        return ReservationNotFoundException.class;
    }

    @Override
    public String getModelName() {
        return "Reservation";
    }

    private Payment generatePayment(final Reservation reservation, final Boolean paymentExist){
        var payment = (paymentExist)
                ? reservationRepository.getPaymentByReservationId(reservation.getId()).orElse(new Payment())
                : new Payment();
        var room = roomRepository.getRoomByIdCompleteFetch(reservation.getRoom().getId()).orElseThrow(
                () -> new RoomNotFoundException("Room doesn't exist"));
        long days = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        payment.setAmount(room.getPricePerNight().multiply(BigDecimal.valueOf(days)));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setReservation(reservation);
        return payment;
    }

    private void payReservation(final Long reservationId) {
        var payment = reservationRepository.getPaymentByReservationId(reservationId).orElseThrow(
                () -> new PaymentNotFoundException("No payment associated with this reservation")
        );
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PAID);
    }

    private void cancelReservation(final Long reservationId) {
        var reservation = reservationRepository.findReservationByIdCompleteFetch(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new DublicateException("Reservation is already canceled");
        }
        var payment = reservation.getPayment();
        if (payment.getPaymentStatus() == PaymentStatus.PAID){
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
        }else{
            payment.setPaymentStatus(PaymentStatus.CANCELLED);
        }
        reservation.setStatus(ReservationStatus.CANCELED);
    }

    private Feedback createFeedbackProcess(final Long reservationId,final Feedback feedback){
        var reservation = reservationRepository.findReservationByIdCompleteFetch(reservationId).orElseThrow(
                () -> new ReservationNotFoundException("No reservation found for ID: " + reservationId)
        );
        feedback.setReservation(reservation);
        feedback.setCreatedAt(LocalDateTime.now());
        reservation.setFeedback(feedback);
        return reservation.getFeedback();
    };

}
