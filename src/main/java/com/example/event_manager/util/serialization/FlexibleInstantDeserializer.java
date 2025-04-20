package com.example.event_manager.util.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class FlexibleInstantDeserializer extends JsonDeserializer<Instant> {
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .appendPattern("[XXX][X][xx][x]")
            .toFormatter();

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        return FORMATTER.parse(text, Instant::from);
    }
}