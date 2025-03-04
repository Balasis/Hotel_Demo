package gr.balasis.hotel.engine.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "gr.balasis.hotel.context.base.entity")
@EnableJpaRepositories(basePackages = "gr.balasis.hotel.engine.core.repository")
public class CoreDatabaseConfiguration {
}
