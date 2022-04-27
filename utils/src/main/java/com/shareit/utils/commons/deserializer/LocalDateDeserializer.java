package com.shareit.utils.commons.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.shareit.utils.commons.exception.InvalidParameterException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LocalDateDeserializer extends StdDeserializer<LocalDate> {

    protected LocalDateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        try {
            return LocalDate.parse(parser.readValueAs(String.class));
        } catch (DateTimeParseException e) {
            throw new InvalidParameterException(parser.getCurrentName());
        }
    }
}
