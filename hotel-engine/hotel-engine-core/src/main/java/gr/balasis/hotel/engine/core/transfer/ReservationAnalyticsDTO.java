package gr.balasis.hotel.engine.core.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"ri","rn","adpr","isf"})
public interface ReservationAnalyticsDTO {
    @JsonProperty("ri")
    Long getRoomId();

    @JsonProperty("rn")
    String getRoomNumber();

    @JsonProperty("adpr")
    Long getAverageDaysPerReservation();

    @JsonProperty("isf")
    Long getIncomeSoFar();
}
