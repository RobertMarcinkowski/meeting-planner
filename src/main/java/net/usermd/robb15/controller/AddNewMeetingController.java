package net.usermd.robb15.controller;

import java.time.ZonedDateTime;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import net.usermd.robb15.model.Meeting;
import net.usermd.robb15.service.MeetingService;

@Controller
public class AddNewMeetingController {

	@Autowired
	MeetingService meetingService;

	@GetMapping("new")
	public ModelAndView newMeetingForm() {
		ModelAndView modelAndView = new ModelAndView("newMeeting");
		Meeting meeting = new Meeting();
		meeting.setTime(new Date());
		meeting.setDuration(15);
		modelAndView.addObject("meeting", meeting);
		return modelAndView;
	}

	@PostMapping("save")
	public ModelAndView addMeetingPost(@ModelAttribute("meeting") @Valid Meeting meeting, BindingResult bindingResult,
			ModelAndView modelAndView) {
		String room = meeting.getRoom().trim().toUpperCase();
		meeting.setRoom(room);

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("newMeeting");
			modelAndView.addObject("meeting", meeting);
			return modelAndView;
		}

		if (meetingService.isRoomReserved(meeting)) {
			ZonedDateTime dateTime = meetingService.findNextFreeTime(meeting);
			modelAndView.setViewName("newMeeting");
			modelAndView.addObject("message", "room is saved for this time, try: " + dateTime);
			modelAndView.addObject("meeting", meeting);
			return modelAndView;
		}

		meetingService.save(meeting);
		modelAndView.setViewName("meetingsList");
		modelAndView.addObject("meetings", meetingService.getAllMeetings());
		modelAndView.addObject("message", "All meetings");
		return modelAndView;
	}
}
