package com.example.demo;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

		User user = (User) session.getAttribute("user");
		List<Message> message = messageRepository.findAll();//全件検索


		session.setAttribute("userscode", 0);
		mv.addObject("message",message);
				mv.setViewName("messageMaster");
				return mv;
	}

	//メッセージを作成
	@RequestMapping("/compose")
	public ModelAndView compose(ModelAndView mv) {

		int code = (int) session.getAttribute("userscode");
		String name = (String) session.getAttribute("name");


		mv.addObject("code",code);
		mv.addObject("name",name);
		mv.setViewName("compose");
		return mv;
	}

	//管理者用メッセージを作成
	@RequestMapping("/composeMaster")
	public ModelAndView composeMaster(ModelAndView mv) {

		int code = (int) session.getAttribute("userscode");
		String name = (String) session.getAttribute("name");


		mv.addObject("code",code);
		mv.addObject("name",name);
		mv.setViewName("composeMaster");
		return mv;
	}
	//メッセージを送信
	@RequestMapping("/send")
	public ModelAndView send(ModelAndView mv,
			@RequestParam("MESSAGE")String message) {


		int code = (int) session.getAttribute("userscode");
		String name = (String) session.getAttribute("name");
//
		mv.addObject("code",code);
		mv.addObject("name",name);

		//今日（予約日）の日付を取得
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);
		Date today = Date.valueOf(str);

		//データベースに登録
		Message newMessage = new Message(code,name,today,message);
		messageRepository.saveAndFlush(newMessage);

		mv.addObject("MESSAGE",message);
		mv.addObject("RESULT","メッセージを送信しました。");
		mv.setViewName("compose");
		return mv;
	}

}
