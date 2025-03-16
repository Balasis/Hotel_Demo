package gr.balasis.hotel.engine.core.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource feedbackMessages() {
        var feedbackMessages = new ReloadableResourceBundleMessageSource();
        feedbackMessages.setBasename("classpath:feedback-messages");
        feedbackMessages.setDefaultEncoding("UTF-8");
        return feedbackMessages;
    }

}
