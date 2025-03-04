package gr.balasis.hotel.engine.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "gr.balasis.hotel.context.base",
        "gr.balasis.hotel.engine.core",
        "gr.balasis.hotel.engine.monitoring",
})
public class HotelCore {
    public static void main(String[] args) {
        SpringApplication.run(HotelCore.class, args);
    }
}
