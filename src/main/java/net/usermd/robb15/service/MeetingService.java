package net.usermd.robb15.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.usermd.robb15.model.Meeting;
import net.usermd.robb15.repository.MeetingRepository;

@Service
public class MeetingService {
	@Autowired
	MeetingRepository meetingRepository;

	@Autowired
	DateTimeService dateTimeService;

	public void save(Meeting meeting) {
		meetingRepository.save(meeting);
	}

	public List<Meeting> getAllMeetings() {
		List<Meeting> meetingsList = new ArrayList<>();
		meetingRepository.findAll().forEach(e -> meetingsList.add(e));
		return meetingsList;
	}

	public Meeting findOne(Long id) {
		return meetingRepository.findOne(id);
	}

	public void delete(Long id) {
		meetingRepository.delete(id);
	}

	public List<Meeting> findTodayMeetings() {

		ZonedDateTime zonedDateStart = dateTimeService.getTodayDate();
		Date start = dateTimeService.convertToDate(zonedDateStart);

		ZonedDateTime zonedDateEnd = zonedDateStart.plusDays(1);
		Date end = dateTimeService.convertToDate(zonedDateEnd);

		return findByTimeBetween(start, end);
	}

	public List<Meeting> findByTimeBetween(Date start, Date end) {
		return meetingRepository.findByTimeBetween(start, end);
	}

	public boolean isRoomReserved(Meeting meeting) {
		String room = meeting.getRoom();
		Integer duration = meeting.getDuration();
		Date newMeetingStart = meeting.getTime();
		Long id = meeting.getId();

		ZoneId defaultZoneId = ZoneId.systemDefault();

		ZonedDateTime zonedNewMeetingStart = dateTimeService.convertToZonedDateTime(newMeetingStart, defaultZoneId);
		ZonedDateTime zonedNewMeetingEnd = zonedNewMeetingStart.plusMinutes(duration);
		ZonedDateTime zonedStartThatCollides = zonedNewMeetingStart.minusMinutes(120);

		List<Meeting> listMeetingsToCheck = findByRoomAndTimeBetween(room, zonedStartThatCollides, zonedNewMeetingEnd);

		for (Meeting meetingToCheck : listMeetingsToCheck) {
			if (!meetingToCheck.getId().equals(id)) {
				Date meetingToCheckStart = meetingToCheck.getTime();
				ZonedDateTime zonedMeetingToCheckStart = dateTimeService.convertToZonedDateTime(meetingToCheckStart,
						defaultZoneId);
				ZonedDateTime zonedMeetingToCheckEnd = zonedMeetingToCheckStart
						.plusMinutes(meetingToCheck.getDuration());
				if (zonedNewMeetingStart.isBefore(zonedMeetingToCheckEnd)
						&& zonedNewMeetingEnd.isAfter(zonedMeetingToCheckStart)) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Meeting> findByRoomAndTimeBetween(String room, ZonedDateTime zonedStart, ZonedDateTime zonedEnd) {
		Date start = dateTimeService.convertToDate(zonedStart);
		Date end = dateTimeService.convertToDate(zonedEnd);
		return findByRoomAndTimeBetween(room, start, end);
	}

	public List<Meeting> findByRoomAndTimeBetween(String room, Date start, Date end) {
		return meetingRepository.findByRoomAndTimeBetweenOrderByTimeAsc(room, start, end);
	}

	public ZonedDateTime findNextFreeTime(Meeting meeting) {
		String room = meeting.getRoom();
		Integer duration = meeting.getDuration();
		Date newMeetingStart = meeting.getTime();
		Long id = meeting.getId();

		ZoneId defaultZoneId = ZoneId.systemDefault();

		ZonedDateTime zonedNewMeetingStart = dateTimeService.convertToZonedDateTime(newMeetingStart, defaultZoneId);
		ZonedDateTime zonedDayEnd = zonedNewMeetingStart.plusDays(1).truncatedTo(ChronoUnit.DAYS).minusMinutes(1);
		ZonedDateTime zonedStartThatCollides = zonedNewMeetingStart.minusMinutes(120);

		List<Meeting> listMeetingsToCheck = findByRoomAndTimeBetween(room, zonedStartThatCollides, zonedDayEnd);

		for (Meeting meetingToCheck : listMeetingsToCheck) {
			if (meetingToCheck.getId().equals(id)) {
				listMeetingsToCheck.remove(meetingToCheck);
			}
		}

		if (listMeetingsToCheck.size() == 0)
			return zonedNewMeetingStart;
		if (listMeetingsToCheck.size() == 1) {
			Date meetingToCheckStart = listMeetingsToCheck.get(0).getTime();
			ZonedDateTime zonedMeetingToCheckStart = dateTimeService.convertToZonedDateTime(meetingToCheckStart,
					defaultZoneId);
			ZonedDateTime zonedMeetingToCheckEnd = zonedMeetingToCheckStart
					.plusMinutes(listMeetingsToCheck.get(0).getDuration());
			return zonedMeetingToCheckEnd;
		}

		Date prevMeetingToCheckStart = listMeetingsToCheck.get(0).getTime();
		ZonedDateTime prevZonedMeetingToCheckStart = dateTimeService.convertToZonedDateTime(prevMeetingToCheckStart,
				defaultZoneId);
		ZonedDateTime prevZonedMeetingToCheckEnd = prevZonedMeetingToCheckStart
				.plusMinutes(listMeetingsToCheck.get(0).getDuration());

		for (int i = 1; i < listMeetingsToCheck.size(); i++) {
			Date meetingToCheckStart = listMeetingsToCheck.get(i).getTime();
			ZonedDateTime zonedMeetingToCheckStart = dateTimeService.convertToZonedDateTime(meetingToCheckStart,
					defaultZoneId);

			long minutesBetween = ChronoUnit.MINUTES.between(prevZonedMeetingToCheckEnd, zonedMeetingToCheckStart);
			if (minutesBetween > duration.longValue())
				return prevZonedMeetingToCheckEnd;

			prevZonedMeetingToCheckEnd = zonedMeetingToCheckStart.plusMinutes(listMeetingsToCheck.get(i).getDuration());

		}
		return prevZonedMeetingToCheckEnd;

	}

}
