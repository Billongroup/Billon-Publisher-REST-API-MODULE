package brum.model.dto.common;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class DateRange {
    private LocalDateTime from;
    private LocalDateTime to;
}
