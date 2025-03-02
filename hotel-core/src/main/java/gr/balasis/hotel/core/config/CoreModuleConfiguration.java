package gr.balasis.hotel.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "gr.balasis.hotel.data.entity")
@EnableJpaRepositories(basePackages = "gr.balasis.hotel.data.repository")
public class CoreModuleConfiguration {
}
