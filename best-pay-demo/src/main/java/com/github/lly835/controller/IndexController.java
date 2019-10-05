package com.github.lly835.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 廖师兄
 * 2019-10-05 23:36
 */
@Controller
public class IndexController {

	@GetMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}
}
