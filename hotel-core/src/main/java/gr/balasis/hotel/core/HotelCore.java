package gr.balasis.hotel.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "gr.balasis.hotel.data.entity")
@ComponentScan(basePackages = {
        "gr.balasis.hotel.context",
        "gr.balasis.hotel.core",
        "gr.balasis.hotel.data"
})
@EnableJpaRepositories(basePackages = "gr.balasis.hotel.data.repository")
public class HotelCore {
    public static void main(String[] args){
        SpringApplication.run(HotelCore.class, args);
    }
}
