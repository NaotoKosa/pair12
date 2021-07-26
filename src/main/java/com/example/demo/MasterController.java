package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MasterController {
	@Autowired
	HttpSession session;

	@Autowired
	MasterRepository masterRepository;

	@Autowired
	ReserveRepository reserveRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	LastMessageRepository lastmessageRepository;

	@Autowired
	MasterLastMessageRepository masterlastmessageRepository;

	//管理者ログイン画面表示
	@RequestMapping(value = "/master", method = RequestMethod.GET)
	public ModelAndView master(ModelAndView mv) {

		mv.setViewName("masterIndex");
		return mv;
	}

	//管理メイン画面表示
	@RequestMapping(value = "/masterMain", method = RequestMethod.GET)
	public ModelAndView masterMain(ModelAndView mv) {
		messageCheckMaster();
		mv.setViewName("masterMain");
		return mv;
	}

	//予約一覧画面表示
	@RequestMapping(value = "/reserve", method = RequestMethod.GET)
	public ModelAndView reserve(ModelAndView mv) {
		//予約データベース（reserve）からデータを取得
		List<Reserve> reserveList = reserveRepository.findAll();
		mv.addObject("reserveList", reserveList);
		mv.setViewName("masterReserve");
		return mv;
	}

	//予約キャンセル
	@RequestMapping(value = "/masterCancel")
	public ModelAndView masterCancel(ModelAndView mv,
			@RequestParam("list.code") int code) {

		reserveRepository.deleteById(code);
		List<Reserve> reserveList = reserveRepository.findAll();
		mv.addObject("reserveList", reserveList);

		mv.setViewName("masterReserve");
		return mv;
	}

	//ユーザ一覧画面表示
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView user(ModelAndView mv) {
		//ユーザデータベース（users）からデータを取得
		List<User> userList = userRepository.findAll();
		mv.addObject("userList", userList);
		mv.setViewName("masterUser");
		return mv;
	}

	//指定したユーザ検索
	@RequestMapping(value = "/user/{userscode}")
	public ModelAndView itemsByCode(
			@PathVariable(name = "userscode") int userscode,
			ModelAndView mv) {
		List<User> userList = userRepository.findByCode(userscode);
		mv.addObject("userList", userList);

		mv.setViewName("masterUser");
		return mv;
	}

	//検索
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView generate(
			@RequestParam(name = "forest", defaultValue = "") String forest,
			@RequestParam(name = "ocean", defaultValue = "") String ocean,
			ModelAndView mv) {

		List<Reserve> reserveList = null;

		if (forest.equals("") && !ocean.equals("")) {
			reserveList = reserveRepository.findByRoom(ocean);
			boolean Ocean = true;
			mv.addObject("Ocean", Ocean);
		} else if (!forest.equals("") && ocean.equals("")) {
			reserveList = reserveRepository.findByRoom(forest);
			boolean Forest = true;
			mv.addObject("Forest", Forest);
		} else {
			reserveList = reserveRepository.findAll();
			boolean Ocean = true;
			boolean Forest = true;
			mv.addObject("Forest", Forest);
			mv.addObject("Ocean", Ocean);
		}

		mv.addObject("reserveList", reserveList);

		mv.setViewName("/masterReserve");
		return mv;
	}

	//管理者用新着メッセージが来ているかチェック
	public void messageCheckMaster() {
		boolean n = false;

		//最後に送られてきたメッセージのCodeを取得
		List<Message> list = messageRepository.findAll();//全件検索
		Message last = list.get(list.size() - 1);
		int lastCode = last.getCode();

		//前回見たメッセージ一覧の最後のCodeを取得
		List<MasterLastMessage> list2 = masterlastmessageRepository.findAll();
		int s = list2.size();
			if(s!=0) {
				MasterLastMessage _last = list2.get(0);
				int _lastCode = _last.getLast();

				//見ていないメッセージがあったとき
				if(lastCode > _lastCode) {
					n = true;
				}
			}
		session.setAttribute("n", n);
	}

}