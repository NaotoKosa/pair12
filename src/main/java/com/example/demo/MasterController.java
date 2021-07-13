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

	//管理者ログイン画面表示
	@RequestMapping(value = "/master", method = RequestMethod.GET)
	public ModelAndView master(ModelAndView mv) {

		mv.setViewName("masterIndex");
		return mv;
	}

	//管理メイン画面表示
	@RequestMapping(value = "/masterMain", method = RequestMethod.GET)
	public ModelAndView masterMain(ModelAndView mv) {

		mv.setViewName("masterMain");
		return mv;
	}

	//ログインを実行
	@RequestMapping(value = "/masterLogin", method = RequestMethod.POST)
	public ModelAndView masterLogin(
			@RequestParam(name = "account", defaultValue = "") String account,
			@RequestParam(name = "password", defaultValue = "") String password,
			ModelAndView mv) {
		//メールアドレス、パスワードが空欄の時にエラーとする
		if (account == null || account.length() == 0) {
			if (password == null || password.length() == 0) {
				mv.addObject("message1", "アカウントを入力してください");
				mv.addObject("message2", "パスワードを入力してください");
				mv.setViewName("masterIndex");
				return mv;
			}
			//メールアドレスが空欄の時にエラーとする
			else {
				mv.addObject("message1", "アカウントを入力してください");
				mv.setViewName("masterIndex");
				return mv;
			}
		}
		//パスワードが空欄の時にエラーとする
		if (password == null || password.length() == 0) {
			mv.addObject("message2", "パスワードを入力してください");
			mv.addObject("account", account);
			mv.setViewName("masterIndex");
			return mv;
		}

		//ログイン処理
		List<Master> master = masterRepository.findByAccount(account);
		// メールアドレスが存在したら
		if (master.size() > 0) {
			// リストの1件目をログインユーザとして取得する
			Master mster = master.get(0);

			String _account = mster.getAccount();
			String _password = mster.getPassword();

			if (account.equals(_account) && password.equals(_password)) {
				//ログイン成功
				String name = mster.getName();

				session.setAttribute("name", name);
				session.setAttribute("master", master);

				mv.addObject("name", name);
				mv.addObject("master", master);

				mv.setViewName("masterMain");
			} else { //メールアドレスとパスワードが不一致 ログインNG
				mv.addObject("RESULT", "メールアドレスとパスワードが一致しませんでした。");
				mv.setViewName("masterIndex");
			}

		} else if (master.size() == 0) {
			// メールアドレスが見つからなかった場合
			// エラーメッセージをセット
			mv.addObject("RESULT", "入力されたメールアドレスは登録されていません");
			mv.setViewName("masterIndex");
		}

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
			@RequestParam(name = "ocean", defaultValue = "" ) String ocean,
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

}
