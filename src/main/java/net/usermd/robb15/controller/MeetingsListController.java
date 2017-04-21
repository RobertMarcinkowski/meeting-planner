package net.usermd.robb15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.usermd.robb15.service.MeetingService;

@Controller
public class MeetingsListController {

	@Autowired
	MeetingService meetingService;

	@RequestMapping("list")
	public ModelAndView meetingsListPage() {
		ModelAndView modelAndView = new ModelAndView("meetingsList");
		modelAndView.addObject("meetings", meetingService.getAllMeetings());
		modelAndView.addObject("message", "All meetings");
		return modelAndView;
	}

}
