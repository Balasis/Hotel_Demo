package gr.balasis.hotel.engine.core.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReservationAnalyticsDTO {
    @JsonProperty("rpr")
    Long getReservationsPerRoom();
}
