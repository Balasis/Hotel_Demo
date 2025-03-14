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

import gr.balasis.hotel.context.base.model.Room;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl extends BasicServiceImpl<Reservation, ReservationNotFoundException> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public Reservation create(final Reservation reservation) {
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setPayment(generatePaymentForReservation(reservation));
        reservationRepository.save(reservation);
        return  reservationRepository.findByIdWithGuestAndRoom(reservation.getId()).orElseThrow(
                () -> new ReservationNotFoundException("Could not find what is just being saved...props to you dev")
        );
    }

    @Override
    public void update(final Reservation reservation) {
        reservation.setPayment(generatePaymentForReservation(reservation));
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> findByGuestId(final Long guestId) {
        return reservationRepository.findByGuestId(guestId);
    }


    @Override
    public Payment getPayment(final  Long reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(
                        () -> new ReservationNotFoundException("Reservation not found for ID: " + reservationId));
        if (reservation.getPayment() == null) {
            throw new PaymentNotFoundException("No payment associated with this reservation");
        }
        return reservation.getPayment();
    }

    @Override
    public Feedback getFeedback(final Long reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(
                        () -> new ReservationNotFoundException("Reservation not found for ID: " + reservationId));

        return Optional.ofNullable(reservation.getFeedback())
                .orElseThrow(
                        () -> new FeedbackNotFoundException("No feedback found for reservation with ID: " + reservationId));
    }

    @Override
    public Feedback createFeedback(final Long reservationId, Feedback feedback) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        reservation.setFeedback(feedback);
        return reservationRepository.save(reservation).getFeedback();
    }

    @Override
    public void updateFeedback(final Long reservationId, Feedback feedback) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        reservation.setFeedback(feedback);
        reservationRepository.save(reservation);
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
    public void manageReservationAction(final Long reservationId, String action) {
        if(action.equalsIgnoreCase(ReservationAction.CANCEL.toString()) ){
            cancelReservation(reservationId);
        }
        if(action.equalsIgnoreCase(ReservationAction.PAY.toString()) ){
            payReservation(reservationId);
        }
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

    private Payment generatePaymentForReservation(final Reservation reservation){
        Payment payment = new Payment();
        System.out.println("The rooms Id is : " + reservation.getRoom().getId());
        Room room = roomRepository.findById(
                reservation.getRoom().getId()).orElseThrow(() -> new RoomNotFoundException("Room doesn't exist"));
        long days = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        payment.setAmount(room.getPricePerNight().multiply(BigDecimal.valueOf(days)));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setReservation(reservation);
        return payment;
    }

    private void cancelReservation(final Long reservationId) {
        var reservation = reservationRepository.findById(reservationId)
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
       reservationRepository.save(reservation);
    }

    private void payReservation(final Long reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        Payment payment = reservation.getPayment();
        if (payment == null) {
            throw new PaymentNotFoundException("No payment associated with this reservation");
        }
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PAID);
        reservationRepository.save(reservation);
    }
}
