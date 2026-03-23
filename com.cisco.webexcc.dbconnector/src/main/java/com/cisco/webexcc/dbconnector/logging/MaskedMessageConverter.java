package com.cisco.webexcc.dbconnector.logging;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.cisco.webexcc.dbconnector.util.LogSanitizer;

public class MaskedMessageConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        return LogSanitizer.sanitize(event.getFormattedMessage());
    }
}