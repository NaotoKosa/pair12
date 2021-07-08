package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	 * 日時選択画面を表示
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
			String roomname = r.getName();

			session.setAttribute("roomcode", roomcode);
			session.setAttribute("roomname", roomname);
		}


		mv.setViewName("info");
		return mv;
	}

	/**
	 *座席確認画面を表示
	 */
	@RequestMapping(value="/seat")
	public ModelAndView seat(ModelAndView mv,
			@RequestParam("date")String date,
			@RequestParam("start")String start,
			@RequestParam("finish")String finish) {

		//予約情報をセッションに格納
		session.setAttribute("date", date);
		session.setAttribute("start", start);
		session.setAttribute("finish", finish);

		//座席選択画面にも予約日時を表示させる
		mv.addObject("date",date);
		mv.addObject("start",start);
		mv.addObject("finish",finish);

		//座席選択画面へ
		mv.setViewName("seat");
		return mv;
	}

	@RequestMapping(value="/seat", method = RequestMethod.POST)
	public ModelAndView seat(ModelAndView mv) {

		//座席選択画面へ
		mv.setViewName("seat");
		return mv;
	}

	/**
	 *予約確認画面を表示
	 */
	@RequestMapping(value="/infocheck")
	public ModelAndView infocheck(
			@RequestParam(name="forest", defaultValue="0") Integer forest,
			ModelAndView mv
	) {
		if(forest == 0) {
			mv.addObject("ERROR", "座席を選択してください。");
			mv.setViewName("seat");
		} else {
		session.setAttribute("forest", forest);
		mv.setViewName("infoCheck");
		}
		return mv;
	}

	/**
	 *予約確認画面を表示
	 */
	@RequestMapping(value="/infoconfirm")
	public ModelAndView infoconfirm(ModelAndView mv) {

		mv.setViewName("infoConfirm");
		return mv;
	}

	@RequestMapping(value="/main")
	public ModelAndView main(ModelAndView mv) {

		mv.setViewName("main");
		return mv;
	}


}
