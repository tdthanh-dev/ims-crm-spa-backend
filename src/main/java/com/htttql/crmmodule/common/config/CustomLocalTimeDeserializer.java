package com.htttql.crmmodule.common.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Custom deserializer for LocalTime to handle multiple formats
 * Supports: "12:20", "1220", "12:20:00"
 */
public class CustomLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    private static final DateTimeFormatter[] FORMATTERS = {
        DateTimeFormatter.ofPattern("HH:mm"),
        DateTimeFormatter.ofPattern("HHmm"),
        DateTimeFormatter.ofPattern("HH:mm:ss"),
        DateTimeFormatter.ofPattern("H:mm"),
        DateTimeFormatter.ofPattern("Hmm")
    };

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        text = text.trim();

        // Try different formats
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalTime.parse(text, formatter);
            } catch (DateTimeParseException e) {
                // Continue to next format
            }
        }

        // If no format works, try to parse manually for formats like "1220"
        try {
            if (text.length() == 4 && text.matches("\\d{4}")) {
                int hour = Integer.parseInt(text.substring(0, 2));
                int minute = Integer.parseInt(text.substring(2, 4));
                return LocalTime.of(hour, minute);
            }
        } catch (Exception e) {
            // Continue
        }

        // If all else fails, throw exception
        throw new IOException("Cannot parse LocalTime from: " + text);
    }
}
