package net.usermd.robb15;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.usermd.robb15.model.Meeting;
import net.usermd.robb15.service.DateTimeService;
import net.usermd.robb15.service.MeetingService;

@SpringBootApplication
public class MeetingPlannerApplication {

	@Autowired
	MeetingService meetingService;

	@Autowired
	DateTimeService dateTimeService;

	public static void main(String[] args) {
		SpringApplication.run(MeetingPlannerApplication.class, args);
	}

	@PostConstruct
	public void initDatabase() {
		Date now = dateTimeService.convertToDate(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES));
		meetingService.save(new Meeting("Cognifide", "Interview in Cognifide", "15A", now, 15));
		meetingService.save(new Meeting("Company", "Interview in Company", "2", now, 30));
		meetingService.save(new Meeting("Company2", "Interview in Company2", "1", now, 60));
	}
}
