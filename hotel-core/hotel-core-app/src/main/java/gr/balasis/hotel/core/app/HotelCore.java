package gr.balasis.hotel.core.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "gr.balasis.hotel.context",
        "gr.balasis.hotel.core.app",
        "gr.balasis.hotel.core.monitoring",
        "gr.balasis.hotel.data"
})
public class HotelCore {
    public static void main(String[] args) {
        SpringApplication.run(HotelCore.class, args);
    }
}
