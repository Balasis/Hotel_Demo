package gr.balasis.hotel.engine.core.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"gi", "tr", "asd", "ti"})
public interface ReservationGuestStatisticsDTO {

    @JsonProperty("gi")
    Long getGuestId();

    @JsonProperty("tr")
    Long getTotalReservations();

    @JsonProperty("asd")
    Double getAvgStayDuration();

    @JsonProperty("ti")
    Double getTotalIncome();

}
