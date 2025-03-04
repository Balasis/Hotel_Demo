package gr.balasis.hotel.engine.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "gr.balasis.hotel.data.entity")
@EnableJpaRepositories(basePackages = "gr.balasis.hotel.core.app.repository")
public class CoreModuleConfiguration {
}
