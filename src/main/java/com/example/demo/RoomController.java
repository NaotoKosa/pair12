package com.example.demo;

import java.time.LocalDate;
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

	@Autowired
	ReserveRepository reserveRepository;

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
			@RequestParam(name="date", defaultValue="1000-01-01")String date,
			@RequestParam("start")String start,
			@RequestParam("finish")String finish) {

		LocalDate dat = LocalDate.parse(date);
		LocalDate todaysDate = LocalDate.now();
		boolean past = todaysDate.isAfter(dat);
		int future = todaysDate.compareTo(dat);
		System.out.println(future);

		if (date.equals("1000-01-01")) {
			mv.addObject("ERROR", "日付けを選択してください。");
			mv.setViewName("info");
		} else if(past==true) {
			mv.addObject("ERROR", "過去は選択できません。");
			mv.setViewName("info");
		} else {
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
		}
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
			@RequestParam(name="seat", defaultValue="0") String seat,
			ModelAndView mv
	) {
		if(seat.equals(0)) {
			mv.addObject("ERROR", "座席を選択してください。");
			mv.setViewName("seat");
		} else {
		session.setAttribute("seat", seat);
		mv.setViewName("infoCheck");
		}
		return mv;
	}

	/**
	 *予約確認画面を表示
	 */
	@RequestMapping(value="/infoconfirm")
	public ModelAndView infoconfirm(ModelAndView mv) {

		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();
		String date = (String) session.getAttribute("date");
		String start = (String) session.getAttribute("start");
		String finish = (String) session.getAttribute("finish");
		String room = (String) session.getAttribute("roomname");
		String seat = (String) session.getAttribute("seat");


		Reserve reserve = new Reserve(userscode,date,start,finish,room,seat);//引数に予約情報を格納


		//予約実行　データベースに登録
		reserveRepository.saveAndFlush(reserve);


		mv.setViewName("infoConfirm");
		return mv;
	}

	@RequestMapping(value="/main")
	public ModelAndView main(ModelAndView mv) {

		mv.setViewName("main");
		return mv;
	}


}
