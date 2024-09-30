package connectripbe.connectrip_be.global.util.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateTimeUtils {

    public static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public String formatUTC(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault())
                .format(DateTimeUtils.UTC_FORMATTER);
    }

}
