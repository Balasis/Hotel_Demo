package gr.balasis.hotel.core.tests.profilemessages;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class RunOnDev {
    private static final Logger logger = LoggerFactory.getLogger(RunOnProd.class);
    private final MessageSource messageSource;

    @Value("${customMessage}")
    private String aBriefMessage;

    @PostConstruct
    public void logMessage() {
        logger.trace(aBriefMessage
                + ", code written on "
                + messageSource.getMessage("app.written.date",null, Locale.getDefault()));
    }
}
