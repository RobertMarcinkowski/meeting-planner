package net.usermd.robb15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.usermd.robb15.service.MeetingService;

@Controller
public class MeetingController {

	@Autowired
	MeetingService meetingService;

	@RequestMapping("meeting/{id}")
	public ModelAndView meetingPage(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView("meeting");
		modelAndView.addObject("meeting", meetingService.findOne(id));
		return modelAndView;
	}

	@RequestMapping("edit/{id}")
	public ModelAndView editMeetingPage(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView("newMeeting");
		modelAndView.addObject("meeting", meetingService.findOne(id));
		return modelAndView;
	}

	@RequestMapping("confirm-delete/{id}")
	public ModelAndView confirmDeleteMeeting(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView("confirm-delete");
		modelAndView.addObject("meeting", meetingService.findOne(id));
		return modelAndView;
	}

	@RequestMapping("delete/{id}")
	public ModelAndView deleteMeeting(@PathVariable Long id) {
		meetingService.delete(id);
		ModelAndView modelAndView = new ModelAndView("meetingsList");
		modelAndView.addObject("meetings", meetingService.getAllMeetings());
		modelAndView.addObject("message", "All meetings");
		return modelAndView;
	}

}
