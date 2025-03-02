package gr.balasis.hotel.core.service;

import gr.balasis.context.base.enums.PaymentStatus;
import gr.balasis.hotel.context.base.domain.Payment;
import gr.balasis.hotel.context.base.mapper.PaymentMapper;
import gr.balasis.hotel.context.web.exception.EntityNotFoundException;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.exception.PaymentNotFoundException;
import gr.balasis.hotel.context.web.exception.ReservationNotFoundException;
import gr.balasis.hotel.context.web.exception.UnauthorizedAccessException;
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
import java.time.format.DateTimeFormatter;
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
        return getReservationAfterCreation(reservation);
    }

    @Override
    public List<Reservation> findReservationsByGuestId(Long id) {
        return reservationRepository.findByGuestId(id).stream()
                .map(reservationMapper::toDomainFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Reservation createReservationForGuest(Long id, Reservation reservation) {
        if(!guestRepository.existsById(reservation.getGuest().getId())){
            throw new EntityNotFoundException("Guest is not found, reservation creation aborted");
        }
        if(id == null || !id.equals(reservation.getGuest().getId())){
            throw new IllegalArgumentException("Provided guest ID does not match the reservation guest ID");
        }
        return getReservationAfterCreation(reservation);

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

    public List<Payment> getReservationPaymentsForGuest(Long guestId) {
        List<ReservationEntity> reservations = reservationRepository.findByGuestId(guestId);

        return reservations.stream()
                .map(ReservationEntity::getPayment)
                .filter(Objects::nonNull)
                .map(paymentMapper::toDomainFromEntity)
                .collect(Collectors.toList());
    }


    @Override
    public JpaRepository<ReservationEntity, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public BaseMapper<Reservation, ReservationResource, ReservationEntity> getMapper() {
        return reservationMapper;
    }

    private Reservation getReservationAfterCreation(Reservation reservation) {
        Payment payment = new Payment();
        if(reservation.getCheckOutDate() != null){
            long daysStayed =ChronoUnit.DAYS.between(reservation.getCheckInDate(),reservation.getCheckOutDate());
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

    private void validateReservationOwnership(Long guestId, ReservationEntity reservationEntity) {
        if (!reservationEntity.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedAccessException("This reservation does not belong to the guest");
        }
    }

    private PaymentEntity getValidPayment(ReservationEntity reservationEntity) {
        PaymentEntity paymentEntity = reservationEntity.getPayment();
        if (paymentEntity == null) {
            throw new PaymentNotFoundException("No payment associated with this reservation");
        }
        return paymentEntity;
    }

}
