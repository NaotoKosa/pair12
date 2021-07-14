package com.example.demo;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MessageController {
	@Autowired
	HttpSession session;

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	UserRepository userRepository;

	//メッセージ一覧を表示
	@RequestMapping("/message")
	public ModelAndView message(ModelAndView mv) {
		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();
		List<Message> message = messageRepository.findByUserscode(userscode);//ログインしているユーザ宛のメッセージのみ表示

		session.setAttribute("userscode", userscode);
		mv.addObject("message",message);
		mv.setViewName("message");
		return mv;
	}

	//管理者用メッセージ一覧表示
	@RequestMapping("/messageMaster")
	public ModelAndView message_master(ModelAndView mv) {

//		User user = (User) session.getAttribute("user");
		List<Message> message = messageRepository.findAll();//全件検索


		session.setAttribute("userscode", 0);
		mv.addObject("message",message);
				mv.setViewName("messageMaster");
				return mv;
	}

	//メッセージを作成
	@RequestMapping("/compose")
	public ModelAndView compose(ModelAndView mv) {

		session.setAttribute("to", "管理者");
		session.setAttribute("email", "master@aaa.com");

		String from = (String) session.getAttribute("name");

		mv.addObject("to","管理者");
		mv.addObject("email","master@aaa.com");
		mv.addObject("from",from);


		mv.setViewName("compose");
		return mv;
	}

	//管理者用メッセージを作成 送信先選択画面へ
	@RequestMapping("/composeMaster")
	public ModelAndView composeMaster(ModelAndView mv) {

			//ユーザデータベース（users）からデータを取得
			List<User> userList = userRepository.findAll();
			mv.addObject("userList", userList);
			mv.setViewName("selectUser");
			return mv;

	}
	//管理者用メッセージを作成
	@RequestMapping("/newMessage")
	public ModelAndView newMessage(ModelAndView mv,
			@RequestParam("userscode")int userscode,
			@RequestParam("to")String to,
			@RequestParam("email")String email
			) {

			mv.addObject("userscode",userscode);
			mv.addObject("to",to);
			mv.addObject("email",email);
			mv.setViewName("composeMaster");
			return mv;
	}

	//メッセージを送信
	@PostMapping("/send")
	public ModelAndView send(ModelAndView mv,
			@RequestParam("MESSAGE")String message) {


		int userscode = (int) session.getAttribute("userscode");
		String from = (String) session.getAttribute("name");
		String to = (String) session.getAttribute("to");
		String email = (String) session.getAttribute("email");

		//今日（予約日）の日付を取得
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);
		Date today = Date.valueOf(str);

		//データベースに登録
		Message newMessage = new Message(today,to,from,userscode,email,message);
		messageRepository.saveAndFlush(newMessage);

		mv.addObject("code",userscode);
		mv.addObject("to",to);
		mv.addObject("email",email);//送信先のメールアドレス
		mv.addObject("from",from);
		mv.addObject("MESSAGE",message);
		mv.addObject("RESULT","メッセージを送信しました。");
		mv.setViewName("compose");
		return mv;
	}

	//管理者用 新規メッセージを送信
	@PostMapping("/sendMaster")
	public ModelAndView sendMaster(ModelAndView mv,
			@RequestParam("userscode")int userscode,
			@RequestParam("to")String to,
			@RequestParam("email")String email,
			@RequestParam("MESSAGE")String message) {

		//今日（予約日）の日付を取得
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);
		Date today = Date.valueOf(str);

		//データベースに登録
		Message newMessage = new Message(today,to,"管理者",userscode,email,message);
		messageRepository.saveAndFlush(newMessage);

		mv.addObject("userscode",userscode);
		mv.addObject("to",to);
		mv.addObject("email",email);//送信先のメールアドレス
		mv.addObject("MESSAGE",message);
		mv.addObject("RESULT","メッセージを送信しました。");
		mv.setViewName("composeMaster");
		return mv;
	}

	//メッセージ返信ページに遷移
	@RequestMapping(value="/reply",method = RequestMethod.GET)
	public ModelAndView reply(ModelAndView mv,
			@RequestParam("code")int code,
			@RequestParam("userscode")int userscode,
			@RequestParam("from")String to,
			@RequestParam("message")String message) {

		List<User> user= (List<User>) userRepository.findByCode(userscode);
		for(User u:user) {
		String email = u.getEmail();
		mv.addObject("email",email);
		}

		mv.addObject("code",code);
		mv.addObject("userscode",userscode);
		mv.addObject("to",to);
		mv.addObject("email","master@aaa.com");
		mv.addObject("message",message);

		mv.setViewName("reply");
		return mv;
	}


	//管理者用　メッセージ返信ページに遷移
		@RequestMapping(value="/replyMaster",method = RequestMethod.GET)
		public ModelAndView replyMaster(ModelAndView mv,
				@RequestParam("code")int code,
				@RequestParam("userscode")int userscode,
				@RequestParam("from")String to,
				@RequestParam("message")String message) {

			List<User> user= (List<User>) userRepository.findByCode(userscode);
			for(User u:user) {
			String email = u.getEmail();
			mv.addObject("email",email);
			}

			mv.addObject("code",code);
			mv.addObject("userscode",userscode);
			mv.addObject("to",to);
			mv.addObject("message",message);

			mv.setViewName("replyMaster");
			return mv;
		}

		//メッセージを返信
		@PostMapping("/reply")
		public ModelAndView reply(ModelAndView mv,
				@RequestParam("to")String to,
				@RequestParam("email")String email,
				@RequestParam("message")String message,
				@RequestParam("MESSAGE")String newmessage) {


			int userscode = (int) session.getAttribute("userscode");
			String from = (String) session.getAttribute("name");

			//今日（予約日）の日付を取得
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String str = sdf.format(timestamp);
			Date today = Date.valueOf(str);

			//データベースに登録
			Message newMessage = new Message(today,to,from,userscode,email,newmessage);
			messageRepository.saveAndFlush(newMessage);

			mv.addObject("to",to);
			mv.addObject("email",email);
			mv.addObject("message",message);
			mv.addObject("MESSAGE",newmessage);
			mv.addObject("RESULT","メッセージを送信しました。");
			mv.setViewName("reply");
			return mv;
		}

		//管理者用　メッセージを返信
				@PostMapping("/doreply")
				public ModelAndView doreply(ModelAndView mv,
						@RequestParam("userscode")int userscode,
						@RequestParam("to")String to,
						@RequestParam("email")String email,
						@RequestParam("message")String message,
						@RequestParam("MESSAGE")String newmessage) {


					//今日（予約日）の日付を取得
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String str = sdf.format(timestamp);
					Date today = Date.valueOf(str);

					//データベースに登録
					Message newMessage = new Message(today,to,"管理者",userscode,email,newmessage);
					messageRepository.saveAndFlush(newMessage);


					mv.addObject("to",to);
					mv.addObject("userscode",userscode);
					mv.addObject("email",email);
					mv.addObject("message",message);
					mv.addObject("MESSAGE",newmessage);
					mv.addObject("RESULT","メッセージを送信しました。");
					mv.setViewName("replyMaster");
					return mv;
				}


}
