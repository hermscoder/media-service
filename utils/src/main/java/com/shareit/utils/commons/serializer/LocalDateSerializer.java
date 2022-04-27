package com.shareit.utils.commons.serializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.shareit.utils.commons.exception.InvalidParameterException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends StdSerializer<LocalDate> {

    public LocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        try {
            generator.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (IOException e) {
            throw new InvalidParameterException(generator.getOutputContext().getCurrentName());
        }
    }
}
