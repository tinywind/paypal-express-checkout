package org.tinywind.paypalexpresscheckout;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.tinywind.paypalexpresscheckout.service.PaypalCommunicationService;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;

class PaypalLoggingTest {
    @Test
    void neverLogsAnUnknownResponseValueOrItsInjectedNewline() throws Exception {
        Logger logger = (Logger) LoggerFactory.getLogger(PaypalCommunicationService.class);
        Level previous = logger.getLevel();
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.setContext(logger.getLoggerContext());
        appender.start();
        logger.addAppender(appender);
        logger.setLevel(Level.TRACE);
        try {
            Method decoder = PaypalCommunicationService.class.getDeclaredMethod("decodeNVP", String.class);
            decoder.setAccessible(true);
            String sensitiveValue = "private-test-value\r\nforged-log-entry";
            decoder.invoke(new PaypalCommunicationService(),
                    "UNKNOWN_FIELD=" + URLEncoder.encode(sensitiveValue, StandardCharsets.UTF_8));
            for (ILoggingEvent event : appender.list) {
                String message = event.getFormattedMessage();
                assertFalse(message.contains("private-test-value"), message);
                assertFalse(message.contains("forged-log-entry"), message);
                assertFalse(message.contains("\n") || message.contains("\r"), message);
            }
        } finally {
            logger.setLevel(previous);
            logger.detachAppender(appender);
            appender.stop();
        }
    }
}
