package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReserveController {

	@Autowired
	HttpSession session;

	@Autowired
	ReserveRepository reserveRepository;

	/**
	 * 予約一覧を表示
	 */
	@RequestMapping(value="/reservation")
	public ModelAndView reserve(ModelAndView mv) {

	//予約データベース（reserve）からデータを取得
		User user = (User) session.getAttribute("user");
		int id = user.getCode();

		//Optional<Reserve> reserveList = reserveRepository.findById(id);
		List<Reserve>reserveList =  reserveRepository.findAll();
		mv.addObject("reserveList", reserveList);

		mv.setViewName("reserve");
		return mv;
	}


	//予約キャンセル
	@RequestMapping(value="/cancel")
	public ModelAndView cancel(ModelAndView mv,
			@RequestParam("list.code")int code) {

		reserveRepository.deleteById(code);
		List<Reserve> reserveList = reserveRepository.findAll();
		mv.addObject("reserveList", reserveList);

		mv.setViewName("reserve");
		return mv;
	}

}
