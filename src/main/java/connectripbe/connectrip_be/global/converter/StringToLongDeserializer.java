package connectripbe.connectrip_be.global.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class StringToLongDeserializer extends StdDeserializer<Long> {

    public StringToLongDeserializer() {
        super(Long.class);
    }

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String value = jsonParser.getText();
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new JsonMappingException(jsonParser, "Unable to convert value to Long: " + value);
        }
    }
}
