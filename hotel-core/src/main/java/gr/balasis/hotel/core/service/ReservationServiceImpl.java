package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.enums.PaymentStatus;
import gr.balasis.hotel.context.base.domain.domains.Payment;
import gr.balasis.hotel.context.base.mapper.PaymentMapper;
import gr.balasis.hotel.context.web.exception.*;
import gr.balasis.hotel.context.base.domain.domains.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.data.entity.PaymentEntity;
import gr.balasis.hotel.data.entity.ReservationEntity;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.mapper.ReservationMapper;
import gr.balasis.hotel.data.repository.GuestRepository;
import gr.balasis.hotel.data.repository.PaymentRepository;
import gr.balasis.hotel.data.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl extends BasicServiceImpl<Reservation, ReservationResource, ReservationEntity> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final PaymentMapper paymentMapper;
    private final GuestRepository guestRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Reservation create(final Reservation reservation) {
        validateGuestExists(reservation.getGuest().getId());
        return buildReservationWithPayment(reservation);
    }

    public Reservation findReservationById(Long guestId, Long reservationId) {
        ReservationEntity reservationEntity = getValidReservation(guestId, reservationId);
        return reservationMapper.toDomainFromEntity(reservationEntity);
    }

    @Override
    @Transactional
    public Reservation createReservationForGuest(Long guestId, Reservation reservation) {
        validateGuestIdMatch(guestId, reservation);
        validateGuestExists(reservation.getGuest().getId());
        return buildReservationWithPayment(reservation);
    }

    @Override
    public List<Reservation> findReservationsByGuestId(Long guestId) {
        validateGuestExists(guestId);
        return reservationRepository.findByGuestId(guestId).stream()
                .map(reservationMapper::toDomainFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelReservation(Long guestId, Long reservationId) {
        ReservationEntity reservationEntity = getValidReservation(guestId, reservationId);

        if (!reservationEntity.getGuest().getId().equals(guestId)) {
            throw new IllegalArgumentException("Reservation does not belong to the guest");
        }
        reservationRepository.delete(reservationEntity);
    }

    @Override
    @Transactional
    public Payment processPaymentForReservation(Long guestId, Long reservationId, Payment payment) {
        ReservationEntity reservationEntity = getValidReservation(guestId, reservationId);
        PaymentEntity paymentEntity = getValidPayment(reservationEntity);
        paymentEntity.setPaymentDate(LocalDateTime.now());
        paymentEntity.setPaymentStatus(PaymentStatus.PAID);
        paymentRepository.save(paymentEntity);

        return paymentMapper.toDomainFromEntity(paymentEntity);
    }

    public Payment getPaymentForReservation(Long guestId, Long reservationId) {
        ReservationEntity reservationEntity = getValidReservation(guestId, reservationId);
        PaymentEntity paymentEntity = getValidPayment(reservationEntity);

        return paymentMapper.toDomainFromEntity(paymentEntity);
    }

    @Override
    public JpaRepository<ReservationEntity, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public BaseMapper<Reservation, ReservationResource, ReservationEntity> getMapper() {
        return reservationMapper;
    }

    private void validateGuestExists(Long guestId) {
        if (!guestRepository.existsById(guestId)) {
            throw new EntityNotFoundException("Guest is not found");
        }
    }

    private void validateGuestIdMatch(Long guestId, Reservation reservation) {
        if (!guestId.equals(reservation.getGuest().getId())) {
            throw new IllegalArgumentException("Provided guest ID does not match the reservation guest ID");
        }
    }

    private void validateReservationOwnership(Long guestId, ReservationEntity reservationEntity) {
        if (!reservationEntity.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedAccessException("This reservation does not belong to the guest");
        }
    }

    private Reservation buildReservationWithPayment(Reservation reservation) {
        Payment payment = new Payment();
        if (reservation.getCheckOutDate() != null) {
            long daysStayed = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
            payment.setAmount(
                    reservation.getRoom().getPricePerNight().multiply(BigDecimal.valueOf(daysStayed))
            );
        }
        payment.setPaymentStatus(PaymentStatus.PENDING);
        reservation.setPayment(payment);
        reservation.setCreatedAt(LocalDateTime.now());
        return reservationMapper.toDomainFromEntity(reservationRepository.save(reservationMapper.toEntity(reservation)));
    }

    private ReservationEntity getValidReservation(Long guestId, Long reservationId) {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        validateReservationOwnership(guestId, reservationEntity);

        return reservationEntity;
    }

    private PaymentEntity getValidPayment(ReservationEntity reservationEntity) {
        PaymentEntity paymentEntity = reservationEntity.getPayment();
        if (paymentEntity == null) {
            throw new PaymentNotFoundException("No payment associated with this reservation");
        }
        return paymentEntity;
    }

}
