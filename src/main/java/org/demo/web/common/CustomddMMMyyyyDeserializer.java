package org.demo.web.common;

/**
 * Created by Asus on 1/7/2020.
 */

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomddMMMyyyyDeserializer extends JsonDeserializer<Date> {
    public CustomddMMMyyyyDeserializer() {
    }

    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        try {
            SimpleDateFormat ex = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            return ex.parse(jp.getText());
        } catch (ParseException var4) {
            Logger.getLogger(CustomddMMMyyyyDeserializer.class.getName()).log(Level.SEVERE, (String)null, var4);
            return null;
        }
    }
}
