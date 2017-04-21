package net.usermd.robb15.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class DateTimeService {
	public ZonedDateTime getTodayDate() {
		return ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);
	}

	public Date convertToDate(ZonedDateTime zonedDateTime) {
		Instant instant = zonedDateTime.toInstant();
		return Date.from(instant);
	}

	public ZonedDateTime convertToZonedDateTime(Date date, ZoneId zoneId) {
		Instant instant = date.toInstant();
		return instant.atZone(zoneId);
	}
}
