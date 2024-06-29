package org.demo.web.common;

/**
 * Created by Asus on 1/7/2020.
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomddMMMyyyySerializer extends JsonSerializer<Date> {
    public CustomddMMMyyyySerializer() {
    }

    public void serialize(Date t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        String formattedDate = formatter.format(t);
        jg.writeString(formattedDate);
    }
}
