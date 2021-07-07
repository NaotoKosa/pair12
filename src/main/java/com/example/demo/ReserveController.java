package com.example.demo;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReserveController {

	@Autowired
	HttpSession session;

	/**
	 * 座席選択画面を表示
	 */
	@RequestMapping(value="/reserve")
	public ModelAndView reserve(ModelAndView mv) {

		mv.setViewName("reserve");
		return mv;
	}
}
