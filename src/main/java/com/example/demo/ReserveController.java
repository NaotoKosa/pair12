package com.example.demo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
			@RequestParam("list.code") int code,
			@RequestParam("list.date") String date,
			@RequestParam("list.start") String start,
			@RequestParam("list.finish") String finish
			) {

		LocalDate dat = LocalDate.parse(date);
		LocalDate todaysDate = LocalDate.now();
		boolean past = todaysDate.isAfter(dat);
		boolean today = todaysDate.isEqual(dat);

		int s = Integer.parseInt(start);
		int f = Integer.parseInt(finish);

		LocalTime a = LocalTime.of(f, 0, 0); //予約した終了時間
		LocalTime n = LocalTime.now(); //現在時刻

		boolean x = n.isAfter(a);

		if(past==true || today == true && x == true) {//選択した日にちが過去or今日のもう終了した時間帯
			mv.addObject("ERROR","この予約はすでに終了しています");
			//予約データベース（reserve）からデータを取得
			User user = (User) session.getAttribute("user");
			int userscode = user.getCode();
			List<Reserve> reserveList = reserveRepository.findByUserscode(userscode);
			mv.addObject("reserveList", reserveList);
		}else {
		reserveRepository.deleteById(code);
		//予約データベース（reserve）からデータを取得
		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();

		List<Reserve> reserveList = reserveRepository.findByUserscode(userscode);
		mv.addObject("reserveList", reserveList);
		}
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
		boolean d = true;//終了ボタンをdisabledに
		mv.addObject("d",d);
		mv.setViewName("checkInOut");
		return mv;
	}

	//利用開始
	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public ModelAndView start(
			@RequestParam(name = "list.code") Integer reservecode,
			@RequestParam(name = "list.start") String _start,
			@RequestParam(name = "list.finish") String _finish,
			ModelAndView mv) {

		int startTime = Integer.parseInt(_start);
		int finishTime = Integer.parseInt(_finish);

		//予約データベース（reserve）からデータを取得
		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);

		List<Reserve> reserveList = reserveRepository.findByUserscodeAndReservedate(userscode, str);
		mv.addObject("reserveList", reserveList);

		List<CheckOut> checkoutList = checkoutRepository.findAll();

		LocalTime a = LocalTime.of(startTime, 0, 0); //予約した開始時間
		LocalTime b = LocalTime.of(finishTime, 0, 0); //予約した終了時間
		LocalTime n = LocalTime.now(); //現在時刻

		boolean x = n.isBefore(a);
		boolean y = n.isAfter(b);

		//現在時刻が開始予約時間より早い時
		if (x == true) {
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
			mv.addObject("ERROR", "この時間はまだ利用できません");
			boolean time = true;
			session.setAttribute("time", time);
			mv.setViewName("checkInOut");
		}
		//現在時刻が終了予約時間より過ぎていた時
			else if (y == true) {
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
			mv.addObject("ERROR", "予約時間を過ぎているため利用できません");
			boolean time = true;
			session.setAttribute("time", time);
			mv.setViewName("checkInOut");
		} else {
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
			boolean time = false;
			session.setAttribute("time", time);
			boolean d = false;//終了ボタンが押せるように
			mv.addObject("d",d);
			mv.setViewName("checkInOut");
		}
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

		boolean time = (boolean) session.getAttribute("time");
		if (time == true) {
			List<CheckIn> checkinList = checkinRepository.findAll();
			List<CheckOut> checkoutList = checkoutRepository.findAll();
			List<Reserve> reserveList = reserveRepository.findByUserscodeAndReservedate(userscode, str);
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
			mv.addObject("reserveList", reserveList);
			mv.addObject("ERROR", "予約時間を過ぎているため利用できません");
			mv.setViewName("checkInOut");
		} else {

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
		}
		return mv;
	}

}