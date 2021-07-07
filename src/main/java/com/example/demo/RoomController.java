package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoomController {

	@Autowired
	HttpSession session;

	@Autowired
	RoomRepository roomRepository;

	/**
	 * 全会議室を表示
	 */
	@RequestMapping(value="/room")
	public ModelAndView room(ModelAndView mv) {
		List<Room> roomList = roomRepository.findAll();
		mv.addObject("room", roomList);

		mv.setViewName("roomlist");
		return mv;
	}

	/**
	 * 座席選択画面を表示
	 */
	@RequestMapping(value="/info")
	public ModelAndView info(
			@RequestParam("CODE") Integer code,
			ModelAndView mv
	) {
		Optional<Room> room = roomRepository.findById(code);

		if (!room.isEmpty()) {
			Room r = room.get();
			Integer roomcode = r.getCode();

			session.setAttribute("roomcode", roomcode);
		}


		mv.setViewName("seat");
		return mv;
	}

	/**
	 *
	 */
	@RequestMapping(value="/seat")
	public ModelAndView seat(ModelAndView mv) {

		mv.setViewName("seat");
		return mv;
	}


}
