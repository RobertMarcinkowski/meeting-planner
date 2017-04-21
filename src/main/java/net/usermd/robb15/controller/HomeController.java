package net.usermd.robb15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.usermd.robb15.service.MeetingService;

@Controller
public class HomeController {

	@Autowired
	MeetingService meetingService;

	@RequestMapping("")
	public ModelAndView startPage() {
		ModelAndView modelAndView = new ModelAndView("meetingsList");
		modelAndView.addObject("meetings", meetingService.findTodayMeetings());
		modelAndView.addObject("message", "Today meetings");
		return modelAndView;
	}

}
