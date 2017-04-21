package net.usermd.robb15.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.usermd.robb15.model.Meeting;

public interface MeetingRepository extends CrudRepository<Meeting, Long> {
	public List<Meeting> findByTimeBetween(Date start, Date end);

	public List<Meeting> findByRoomAndTimeBetweenOrderByTimeAsc(String room, Date start, Date end);
}
