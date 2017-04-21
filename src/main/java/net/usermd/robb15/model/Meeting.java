package net.usermd.robb15.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Meeting {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 1, message = "name is required")
	private String name;

	private String description;

	@NotNull
	@Size(min = 1, message = "room is required")
	private String room;

	@NotNull(message = "time is required, format pattern: yyyy/MM/dd HH:mm")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	private Date time;

	@NotNull(message = "duration is required")
	@Min(15)
	@Max(120)
	private Integer duration;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Meeting(String name, String description, String room, Date time, Integer duration) {
		super();
		this.name = name;
		this.description = description;
		this.room = room;
		this.time = time;
		this.duration = duration;
	}

	public Meeting() {
		super();
	}

}
