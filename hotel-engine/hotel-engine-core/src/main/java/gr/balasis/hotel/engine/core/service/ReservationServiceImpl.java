package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import gr.balasis.hotel.context.base.enumeration.ReservationAction;
import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.base.exception.HotelException;
import gr.balasis.hotel.context.base.exception.business.ReservationPaymentException;
import gr.balasis.hotel.context.base.exception.duplicate.DuplicateException;
import gr.balasis.hotel.context.base.exception.notfound.FeedbackNotFoundException;
import gr.balasis.hotel.context.base.exception.notfound.PaymentNotFoundException;
import gr.balasis.hotel.context.base.exception.notfound.ReservationNotFoundException;
import gr.balasis.hotel.context.base.exception.notfound.RoomNotFoundException;
import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.context.base.service.BasicServiceImpl;
import gr.balasis.hotel.engine.core.repository.ReservationRepository;
import gr.balasis.hotel.engine.core.repository.RoomRepository;
import gr.balasis.hotel.engine.core.transfer.KeyValue;
import gr.balasis.hotel.engine.core.transfer.ReservationGuestStatisticsDTO;
import gr.balasis.hotel.engine.core.transfer.ReservationRoomStatisticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class ReservationServiceImpl extends BasicServiceImpl<Reservation, ReservationNotFoundException> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public Reservation get(final Long reservationId) {
        return reservationRepository.findByIdCompleteFetch(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("No reservation found for ID: " + reservationId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByGuestId(final Long guestId) {
        return reservationRepository.findByGuestIdCompleteFetch(guestId);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAllCompleteFetch();
    }

    @Override
    @Transactional(readOnly = true)
    public Feedback getFeedback(final Long reservationId) {
        return reservationRepository.getFeedbackByReservationId(reservationId)
                .orElseThrow(() -> new FeedbackNotFoundException("No feedback found for reservation with ID: " + reservationId));
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPayment(final Long reservationId) {
        return reservationRepository.getPaymentByReservationId(reservationId)
                .orElseThrow(() -> new PaymentNotFoundException("No payment found for reservation with ID: " + reservationId));
    }


    @Override
    public Reservation create(final Reservation reservation) {
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.ACTIVE);
        var savedReservation = reservationRepository.save(reservation);
        savedReservation.setPayment(generatePayment(savedReservation, false));
        return reservationRepository.findByIdCompleteFetch(savedReservation.getId()).orElseThrow(
                () -> new ReservationNotFoundException("Failed to fetch the reservation with" +
                        " associated guest and room details after saving."));
    }

    @Override
    public Feedback createFeedback(final Long reservationId, final Feedback feedback) {
        var savedReservation = reservationRepository.findByIdMinimalFetch(reservationId).orElseThrow(
                () -> new ReservationNotFoundException("Could not find reservation with id: " + reservationId));
        savedReservation.setFeedback(feedback);
        savedReservation.setCreatedAt(LocalDateTime.now());
        return savedReservation.getFeedback();
    }

    @Override
    public void updateFeedback(final Long reservationId, final Feedback feedback) {
        reservationRepository.updateFeedback(reservationId, feedback.getMessage(), LocalDateTime.now());
    }

    @Override
    public void update(final Reservation reservation) {
        var savedReservation = reservationRepository.findByIdCompleteFetch(reservation.getId()).orElseThrow(
                () -> new ReservationNotFoundException("Reservation to update not found")
        );
        savedReservation.setRoom(reservation.getRoom());
        savedReservation.setGuest(reservation.getGuest());
        savedReservation.setPayment(generatePayment(savedReservation, true));
    }

    @Override
    public void manageReservationAction(final Long reservationId, String action) {
        if (action.equalsIgnoreCase(ReservationAction.CANCEL.toString())) {
            cancelReservation(reservationId);
        }
        if (action.equalsIgnoreCase(ReservationAction.PAY.toString())) {
            payReservation(reservationId);
        }
    }

    @Override
    public void deleteFeedback(final Long feedbackId) {
        reservationRepository.deleteFeedbackById(feedbackId);
    }

    @Override
    public List<ReservationGuestStatisticsDTO> findGuestStatistics() {
        return reservationRepository.findReservationGuestStatistics();
    }

    @Override
    public List<ReservationRoomStatisticsDTO> findRoomStatistics() {
        return reservationRepository.findReservationRoomStatistics();
    }

    @Override
    public KeyValue<String, Float> getAvgPercentageRateOfFeedback() {
        return reservationRepository.getAvgPercentageRateOfFeedback();
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

    private Payment generatePayment(final Reservation reservation, final Boolean paymentExist) {
        var payment = (paymentExist)
                ? reservationRepository.getPaymentByReservationId(reservation.getId()).orElse(new Payment())
                : new Payment();
        var room = roomRepository.getRoomByIdCompleteFetch(reservation.getRoom().getId()).orElseThrow(
                () -> new RoomNotFoundException("Room doesn't exist"));
        long days = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        reservation.setRoom(room);
        payment.setAmount(room.getPricePerNight().multiply(BigDecimal.valueOf(days)));
        payment.setAmount(BigDecimal.valueOf(100L));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setReservation(reservation);
        return payment;
    }

    private void payReservation(final Long reservationId) {
        var payment = reservationRepository.getPaymentByReservationId(reservationId).orElseThrow(
                () -> new PaymentNotFoundException("No payment associated with this reservation")
        );
        if (payment.getPaymentStatus().equals(PaymentStatus.PAID)) {
            {
                throw new ReservationPaymentException("Reservation is already paid");
            }
        }
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PAID);
    }

    private void cancelReservation(final Long reservationId) {
        var reservation = reservationRepository.findByIdMinimalFetch(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new DuplicateException("Reservation is already canceled");
        }
        var payment = reservation.getPayment();
        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
        } else {
            payment.setPaymentStatus(PaymentStatus.CANCELLED);
        }
        reservation.setStatus(ReservationStatus.CANCELED);
    }

}
