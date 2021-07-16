package com.example.demo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReserveController {

	@Autowired
	HttpSession session;
	@Autowired
	ReserveRepository reserveRepository;
	@Autowired
	CheckInRepository checkinRepository;
	@Autowired
	CheckOutRepository checkoutRepository;

	//予約一覧を表示
	@RequestMapping(value = "/reservation")
	public ModelAndView reserve(ModelAndView mv) {

		//予約データベース（reserve）からデータを取得
		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();

		List<Reserve> reserveList = reserveRepository.findByUserscode(userscode);
		mv.addObject("reserveList", reserveList);

//		for (Reserve r : reserveList) {
//			Integer rc = r.getCode();
//			for(Reserve rr : reserveList) {
//				Integer rrc = rr.getCode();
//				if(rc == rrc) {
//					String rd = rr.getDate();
//					LocalDate dat = LocalDate.parse(rd);
//					LocalDate todaysDate = LocalDate.now();
//					boolean past = todaysDate.isAfter(dat);
//
//					mv.addObject("past", past);
//				}
//			}
//		}




		mv.setViewName("reserve");
		return mv;
	}

	//予約キャンセル
	@RequestMapping(value = "/cancel")
	public ModelAndView cancel(ModelAndView mv,
			@RequestParam("list.code") int code) {

		reserveRepository.deleteById(code);
		List<Reserve> reserveList = reserveRepository.findAll();
		mv.addObject("reserveList", reserveList);

		mv.setViewName("reserve");
		return mv;
	}

	//メイン画面から本日の予約一覧へ
	@RequestMapping(value = "/checkInOut")
	public ModelAndView checkin(ModelAndView mv) {

		//予約データベース（reserve）からデータを取得
		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);

		List<Reserve> reserveList = reserveRepository.findByUserscodeAndReservedate(userscode, str);
		mv.addObject("reserveList", reserveList);

		List<CheckIn> checkinList = checkinRepository.findAll();

		List<CheckOut> checkoutList = checkoutRepository.findAll();

		for (CheckIn c : checkinList) {
			for (Reserve r : reserveList) {
				Integer rc = r.getCode();
				Integer cc = c.getReservecode();
				if (rc == cc) {
					r.setCheckinStart(c.getStart());
				}
			}
		}
		for (CheckOut c : checkoutList) {
			for (Reserve r : reserveList) {
				Integer rc = r.getCode();
				Integer cc = c.getReservecode();
				if (rc == cc) {
					r.setCheckoutFinish(c.getFinish());
				}
			}
		}

		mv.setViewName("checkInOut");
		return mv;
	}

	//利用開始
	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public ModelAndView start(
			@RequestParam(name = "list.code") Integer reservecode,
			ModelAndView mv) {

		//予約データベース（reserve）からデータを取得
		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);

		List<Reserve> reserveList = reserveRepository.findByUserscodeAndReservedate(userscode, str);
		mv.addObject("reserveList", reserveList);

		List<CheckOut> checkoutList = checkoutRepository.findAll();

		//現在時刻を取得
		LocalDateTime start = LocalDateTime.now();

		CheckIn in = new CheckIn(userscode, reservecode, start);
		checkinRepository.saveAndFlush(in);

		List<CheckIn> checkinList = checkinRepository.findAll();

		for (CheckIn c : checkinList) {
			for (Reserve r : reserveList) {
				Integer rc = r.getCode();
				Integer cc = c.getReservecode();
				if (rc == cc) {
					r.setCheckinStart(c.getStart());
				}
			}
		}
		for (CheckOut c : checkoutList) {
			for (Reserve r : reserveList) {
				Integer rc = r.getCode();
				Integer cc = c.getReservecode();
				if (rc == cc) {
					r.setCheckoutFinish(c.getFinish());
				}
			}
		}

		mv.setViewName("checkInOut");
		return mv;
	}

	//利用終了
	@RequestMapping(value = "/finish", method = RequestMethod.POST)
	public ModelAndView finish(
			@RequestParam(name = "list.code") Integer reservecode,
			ModelAndView mv) {

		//予約データベース（reserve）からデータを取得
		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);

		List<Reserve> reserveList = reserveRepository.findByUserscodeAndReservedate(userscode, str);
		mv.addObject("reserveList", reserveList);

		List<CheckIn> checkinList = checkinRepository.findAll();

		//現在時刻を取得
		LocalDateTime finish = LocalDateTime.now();

		CheckOut out = new CheckOut(userscode, reservecode, finish);
		checkoutRepository.saveAndFlush(out);
		List<CheckOut> checkoutList = checkoutRepository.findAll();

		for (CheckIn c : checkinList) {
			for (Reserve r : reserveList) {
				Integer rc = r.getCode();
				Integer cc = c.getReservecode();
				if (rc == cc) {
					r.setCheckinStart(c.getStart());
				}
			}
		}
		for (CheckOut c : checkoutList) {
			for (Reserve r : reserveList) {
				Integer rc = r.getCode();
				Integer cc = c.getReservecode();
				if (rc == cc) {
					r.setCheckoutFinish(c.getFinish());
				}
			}
		}

		mv.setViewName("checkInOut");
		return mv;
	}

}