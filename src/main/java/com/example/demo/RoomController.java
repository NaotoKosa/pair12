package com.example.demo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

	@Autowired
	ReserveRepository reserveRepository;

	/**
	 * 全会議室を表示
	 */
	@RequestMapping(value = "/room")
	public ModelAndView room(ModelAndView mv) {
		List<Room> roomList = roomRepository.findAll();
		mv.addObject("room", roomList);

		mv.setViewName("roomlist");
		return mv;
	}

	/**
	 * 日時選択画面を表示
	 */
	@RequestMapping(value = "/info")
	public ModelAndView info(
			@RequestParam("CODE") Integer code,
			ModelAndView mv) {
		Optional<Room> room = roomRepository.findById(code);

		if (!room.isEmpty()) {
			Room r = room.get();
			Integer roomcode = r.getCode();
			String roomname = r.getName();

			session.setAttribute("roomcode", roomcode);
			session.setAttribute("roomname", roomname);
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		LocalDate calendarDate = LocalDate.now();
		mv.addObject("calendarDate", calendarDate);


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

		int s = Integer.parseInt(start);
		int f = Integer.parseInt(finish);
		User _user = (User) session.getAttribute("user");
		int userscode = _user.getCode();


		String reserReservedate = null;
		String reserStart = "0";
		String reserFinish = "0";
		boolean re = false;
		List<Reserve> reserve = reserveRepository.findByUserscode(userscode);
		if(!reserve.isEmpty()) {

			for(Reserve r: reserve) {
				reserReservedate = r.getReservedate();
				reserStart = r.getStart();
				reserFinish = r.getFinish();
				int reStart = Integer.parseInt(reserStart);
				int reFinish = Integer.parseInt(reserFinish);

				if(date.equals(reserReservedate) &&(s == reStart || f == reFinish || s < reFinish && f > reStart) ) {
					mv.addObject("ERROR", "この時間はすでに予約しています。");
					re = true;
				}

			}
		}



//		String url = "jdbc:postgresql:zaseki_db"; //接続するDBのURL
//		String user = "postgres"; //ユーザ名
//		String pass = "himitu"; //パスワード
//
//		try {
//			Class.forName("org.postgresql.Driver");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			System.out.println("JDBCドライバが登録されていないよ！");
//		}
//
//
//		String sql = "SELECT * FROM reserve WHERE reservedate = ? and (start = ? or finish = ? or start < ? and finish > ?) and userscode = ?";//重複チェック
//
//		try (
//			//データベースへの接続
//			Connection con = DriverManager.getConnection(url, user, pass);
//			//SQL文の実行の準備をして実行に備える
//			PreparedStatement ps = con.prepareStatement(sql);
//		){	ps.setString(1,date);
//		ps.setString(2,start);
//		ps.setString(3,finish);
//		ps.setString(4,finish);
//		ps.setString(5,start);
//		ps.setInt(6,userscode);
//
//		boolean reserved = false;
//
//		//SQL文の実行
//		//SELECT文を実行する
//		ResultSet rs = ps.executeQuery();
//
//		if (rs.next() == true) {
//
//			reserved = true;
//		}

		if (date.equals("1000-01-01")) {
			mv.addObject("ERROR", "日付けを選択してください。");
			mv.setViewName("info");
		} else if(past==true) {
			mv.addObject("ERROR", "過去は選択できません。");
			mv.setViewName("info");
		} else if(s>= f) {
			mv.addObject("TIMEERROR", "時間設定を見直してください。");
			mv.setViewName("info");
		} else if(re){
			mv.setViewName("info");
		}
		else {
		//	String date = (String) session.getAttribute("date");
//			String start = (String) session.getAttribute("start");
//			String finish = (String) session.getAttribute("finish");
			String room = (String) session.getAttribute("roomname");


			String url = "jdbc:postgresql:zaseki_db"; //接続するDBのURL
			String user = "postgres"; //ユーザ名
			String pass = "himitu"; //パスワード

			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("JDBCドライバが登録されていないよ！");
			}



			String sql = "SELECT * FROM reserve WHERE reservedate = ? and (start = ? or finish = ? or start < ? and finish > ?) and room = ?";//重複チェック

			try (
				//データベースへの接続
				Connection con = DriverManager.getConnection(url, user, pass);
				//SQL文の実行の準備をして実行に備える
				PreparedStatement ps = con.prepareStatement(sql);
			){
				//プレースホルダの部分に値を設定
				ps.setString(1,date);
				ps.setString(2,start);
				ps.setString(3,finish);
				ps.setString(4,finish);
				ps.setString(5,start);
				ps.setString(6,room);
			//	ps.setString(7,seat);
				//SQL文の実行
				//SELECT文を実行する
				ResultSet rs = ps.executeQuery();

				boolean s1 = false;
				boolean s2 = false;
				boolean s3 = false;
				boolean s4 = false;
				boolean s5 = false;
				boolean s6 = false;
				boolean s7 = false;
				boolean s8 = false;
				boolean s9 = false;
				boolean s10 = false;
				boolean s11 = false;
				boolean s12 = false;
				boolean s13 = false;
				boolean s14 = false;
				boolean s15 = false;
				boolean s16 = false;
				boolean s17 = false;
				boolean s18 = false;
				boolean s19 = false;
				boolean s20 = false;
				boolean s21 = false;
				boolean s22 = false;
				boolean s23 = false;
				boolean s24 = false;
				boolean s25 = false;
				boolean s26 = false;
				boolean s27 = false;
				boolean s28 = false;
				boolean s29 = false;
				boolean s30 = false;
				boolean s31 = false;
				boolean s32 = false;
				boolean s33 = false;
				boolean s34 = false;
				boolean s35 = false;
				boolean s36 = false;
				boolean s37 = false;
				boolean s38 = false;
				boolean s39 = false;
				boolean s40 = false;
				boolean s41 = false;
				boolean s42 = false;


			if (rs.next() == true) {//データがあったら(同じ時間に予約が入っていたら)実行

				do {
					System.out.println(rs.getString("seat"));
						if(rs.getString("seat").equals("1")) {
							s1 = true;
						}else if(rs.getString("seat").equals("2")) {
							s2 = true;
						}else if(rs.getString("seat").equals("3")) {
							s3 = true;
						}else if(rs.getString("seat").equals("4")) {
							s4 = true;
						}else if(rs.getString("seat").equals("5")) {
							s5 = true;
						}else if(rs.getString("seat").equals("6")) {
							s6 = true;
						}else if(rs.getString("seat").equals("7")) {
							s7 = true;
						}else if(rs.getString("seat").equals("8")) {
							s8 = true;
						}else if(rs.getString("seat").equals("9")) {
							s9 = true;
						}else if(rs.getString("seat").equals("10")) {
							s10 = true;
						}else if(rs.getString("seat").equals("11")) {
							s11 = true;
						}else if(rs.getString("seat").equals("12")) {
							s12 = true;
						}else if(rs.getString("seat").equals("13")) {
							s13 = true;
						}else if(rs.getString("seat").equals("14")) {
							s14 = true;
						}else if(rs.getString("seat").equals("15")) {
							s15 = true;
						}else if(rs.getString("seat").equals("16")) {
							s16 = true;
						}else if(rs.getString("seat").equals("17")) {
							s17 = true;
						}else if(rs.getString("seat").equals("18")) {
							s18 = true;
						}else if(rs.getString("seat").equals("19")) {
							s19 = true;
						}else if(rs.getString("seat").equals("20")) {
							s20 = true;
						}else if(rs.getString("seat").equals("21")) {
							s21 = true;
						}else if(rs.getString("seat").equals("22")) {
							s22 = true;
						}else if(rs.getString("seat").equals("23")) {
							s23 = true;
						}else if(rs.getString("seat").equals("24")) {
							s24 = true;
						}else if(rs.getString("seat").equals("25")) {
							s25 = true;
						}else if(rs.getString("seat").equals("26")) {
							s26 = true;
						}else if(rs.getString("seat").equals("27")) {
							s27 = true;
						}else if(rs.getString("seat").equals("28")) {
							s28 = true;
						}else if(rs.getString("seat").equals("29")) {
							s29 = true;
						}else if(rs.getString("seat").equals("30")) {
							s30 = true;
						}else if(rs.getString("seat").equals("31")) {
							s31 = true;
						}else if(rs.getString("seat").equals("32")) {
							s32 = true;
						}else if(rs.getString("seat").equals("33")) {
							s33 = true;
						}else if(rs.getString("seat").equals("34")) {
							s34 = true;
						}else if(rs.getString("seat").equals("35")) {
							s35 = true;
						}else if(rs.getString("seat").equals("36")) {
							s36 = true;
						}else if(rs.getString("seat").equals("37")) {
							s37 = true;
						}else if(rs.getString("seat").equals("38")) {
							s38 = true;
						}else if(rs.getString("seat").equals("39")) {
							s39 = true;
						}else if(rs.getString("seat").equals("40")) {
							s40 = true;
						}else if(rs.getString("seat").equals("41")) {
							s41 = true;
						}else if(rs.getString("seat").equals("42")) {
							s42 = true;
						}

				}while(rs.next() == true);


				mv.addObject("s1",s1);
				mv.addObject("s2",s2);
				mv.addObject("s3",s3);
				mv.addObject("s4",s4);
				mv.addObject("s5",s5);
				mv.addObject("s6",s6);
				mv.addObject("s7",s7);
				mv.addObject("s8",s8);
				mv.addObject("s9",s9);
				mv.addObject("s10",s10);
				mv.addObject("s11",s11);
				mv.addObject("s12",s12);
				mv.addObject("s13",s13);
				mv.addObject("s14",s14);
				mv.addObject("s15",s15);
				mv.addObject("s16",s16);
				mv.addObject("s17",s17);
				mv.addObject("s18",s18);
				mv.addObject("s19",s19);
				mv.addObject("s20",s20);
				mv.addObject("s21",s21);
				mv.addObject("s22",s22);
				mv.addObject("s23",s23);
				mv.addObject("s24",s24);
				mv.addObject("s25",s25);
				mv.addObject("s26",s26);
				mv.addObject("s27",s27);
				mv.addObject("s28",s28);
				mv.addObject("s29",s29);
				mv.addObject("s30",s30);
				mv.addObject("s31",s31);
				mv.addObject("s32",s32);
				mv.addObject("s33",s33);
				mv.addObject("s34",s34);
				mv.addObject("s35",s35);
				mv.addObject("s36",s36);
				mv.addObject("s37",s37);
				mv.addObject("s38",s38);
				mv.addObject("s39",s39);
				mv.addObject("s40",s40);
				mv.addObject("s41",s41);
				mv.addObject("s42",s42);

//				if (rs.next() == true) {        //データがあったら(同じ時間に予約が入っていたら)実行
//
//					List<String> list = new ArrayList();
//					list.add(rs.getString("seat"));//reserveテーブルのseat列の値（席番号）をStringで取得
//
//					while(rs.next() == true) {
//						list.add(rs.getString("seat"));//reserveテーブルのseat列の値（席番号）をStringで取得
//
//					}
//					mv.addObject("s",list);

//					for(String l:list) {
//
//						String s = "seat"+l;
//
//						session.setAttribute("s", );
//					}

//					session.setAttribute("seat", reserved);

					mv.setViewName("seat");

				}else {//同じ時間帯に予約が入っていなければそのまま座席選択画面に遷移

					mv.setViewName("seat");
				}


			} catch (SQLException e) {
				e.printStackTrace();
			}


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

//	@RequestMapping(value = "/seat", method = RequestMethod.POST)
//	public ModelAndView seat(ModelAndView mv) {
//
//		//座席選択画面へ
//		mv.setViewName("seat");
//		return mv;
//	}

	/**
	 *予約確認画面を表示
	 */
	@SuppressWarnings("null")
	@RequestMapping(value = "/infocheck")
	public ModelAndView infocheck(
			@RequestParam(name = "seat", defaultValue = "0") String seat,
			ModelAndView mv) {
		if (seat.equals("0")) {
			mv.addObject("ERROR", "座席を選択してください。");
			mv.setViewName("seat");
		} else {

			//			String date = (String) session.getAttribute("date");
			//			String start = (String) session.getAttribute("start");
			//			String finish = (String) session.getAttribute("finish");
			//			String room = (String) session.getAttribute("roomname");
			//
			//
			//			String url = "jdbc:postgresql:zaseki_db"; //接続するDBのURL
			//			String user = "postgres"; //ユーザ名
			//			String pass = "himitu"; //パスワード
			//
			//			try {
			//				Class.forName("org.postgresql.Driver");
			//			} catch (ClassNotFoundException e) {
			//				e.printStackTrace();
			//				System.out.println("JDBCドライバが登録されていないよ！");
			//			}
			//
			//
			////String sql = "SELECT * FROM reserve WHERE reservedate = ? and (start = ? or finish = ? or start < ? and finish > ?) and room = ? and seat = ?";//重複チェック
			//			String sql = "SELECT * FROM reserve WHERE reservedate = ? and (start = ? or finish = ? or start < ? and finish > ?) and room = ?";//重複チェック
			//
			//			try (
			//				//データベースへの接続
			//				Connection con = DriverManager.getConnection(url, user, pass);
			//				//SQL文の実行の準備をして実行に備える
			//				PreparedStatement ps = con.prepareStatement(sql);
			//			){
			//				//プレースホルダの部分に値を設定
			//				ps.setString(1,date);
			//				ps.setString(2,start);
			//				ps.setString(3,finish);
			//				ps.setString(4,finish);
			//				ps.setString(5,start);
			//				ps.setString(6,room);
			//			//	ps.setString(7,seat);
			//				//SQL文の実行
			//				//SELECT文を実行する
			//				ResultSet rs = ps.executeQuery();
			//
			//
			//
			//				if (rs.next() == true) {        //データがあったら実行
			//					mv.addObject("ERROR","この席はすでに予約が入っています。");
			//					List<String> list = new ArrayList();
			//					list.add(rs.getString("seat"));//reserveテーブルのseat列の値（席番号）をStringで取得
			//
			//					while(rs.next() == true) {
			//						list.add(rs.getString("seat"));//reserveテーブルのseat列の値（席番号）をStringで取得
			//
			//					}
			//
			//					System.out.println(list);
			//					mv.setViewName("seat");
			//
			//				}else {
			//					session.setAttribute("seat", seat);
			//					mv.setViewName("infoCheck");
			//				}
			//
			//			} catch (SQLException e) {
			//				e.printStackTrace();
			//			}

			session.setAttribute("seat", seat);
			mv.setViewName("infoCheck");

		}
		return mv;
	}

	/**
	 *予約確認画面を表示
	 */
	@RequestMapping(value = "/infoconfirm")
	public ModelAndView infoconfirm(ModelAndView mv) {

		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();
		String date = (String) session.getAttribute("date");
		String start = (String) session.getAttribute("start");
		String finish = (String) session.getAttribute("finish");
		String room = (String) session.getAttribute("roomname");
		String seat = (String) session.getAttribute("seat");

		//今日（予約日）の日付を取得
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);
		Date today = Date.valueOf(str);

		Reserve reserve = new Reserve(userscode, today, date, start, finish, room, seat);//引数に予約情報を格納

		//予約実行　データベースに登録
		reserveRepository.saveAndFlush(reserve);

		mv.setViewName("infoConfirm");
		return mv;
	}

	@RequestMapping(value = "/main")
	public ModelAndView main(ModelAndView mv) {

		mv.setViewName("main");
		return mv;
	}

}
