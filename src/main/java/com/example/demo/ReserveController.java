package com.example.demo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
		int userscode = user.getCode();


		List<Reserve> reserveList = reserveRepository.findByUserscode(userscode);
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

//    public static String toStr(LocalDate localDate, String format) {
//
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
//        return localDate.format(dateTimeFormatter);
//
//    }

	//メイン画面から本日の予約一覧へ
	@RequestMapping(value="/checkInOut")
	public ModelAndView checkin(ModelAndView mv) {

		//予約データベース（reserve）からデータを取得
				User user = (User) session.getAttribute("user");
				int userscode = user.getCode();

				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String str = sdf.format(timestamp);

				//List<Reserve> reserveList = reserveRepository.findByUserscode(userscode);
				List<Reserve> reserveList = reserveRepository.findByUserscodeAndReservedate(userscode,str);
				mv.addObject("reserveList", reserveList);

				boolean s = false;
				boolean t = true;
				mv.addObject("s",s);
				mv.addObject("t",t);
				mv.setViewName("checkin");
				return mv;
	}

	//利用開始
	@RequestMapping(value="/start")
	public ModelAndView start(ModelAndView mv) {

		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);

		List<Reserve> reserveList = reserveRepository.findByUserscodeAndReservedate(userscode,str);
		mv.addObject("reserveList", reserveList);

		//現在時刻を取得
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		Date start = new Date();

		session.setAttribute("START", start);
		mv.addObject("START",start);
		boolean s  = true;
		boolean t  = false;
		mv.addObject("s",s);
		mv.addObject("t",t);
		mv.setViewName("checkin");
				return mv;
	}


	//利用終了
	@RequestMapping(value="/finish")
	public ModelAndView finish(ModelAndView mv) {
		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);

		List<Reserve> reserveList = reserveRepository.findByUserscodeAndReservedate(userscode,str);
		mv.addObject("reserveList", reserveList);

		//現在時刻を取得
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		Date finish = new Date();

		boolean s  = true;
		boolean t  = true;
		mv.addObject("s",s);
		mv.addObject("t",t);

		mv.addObject("FINISH",finish);

		session.setAttribute("FINISH", finish);
		mv.setViewName("checkin");
				return mv;
	}



}