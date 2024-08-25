package connectripbe.connectrip_be.global.component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class DateTimeComponent {

      private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

      public String formatToUTC(LocalDateTime dateTime) {
            if (dateTime == null) {
                  return null;
            }
            return dateTime.atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC"))
                    .format(UTC_FORMATTER);
      }
}
