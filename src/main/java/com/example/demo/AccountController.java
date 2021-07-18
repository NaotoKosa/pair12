package com.example.demo;

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
public class AccountController {
	@Autowired
	HttpSession session;

	@Autowired
	UserRepository userRepository;

	//トップページ（ログイン画面）を表示
	@RequestMapping("/")
	public String top() {
		// セッション情報はクリアする
		session.invalidate();
		return "top";
	}

	//トップページ（ログイン画面）を表示
	@RequestMapping("/home")
	public String home() {
		// セッション情報はクリアする
		session.invalidate();
		return "home";
	}

	//ログインを実行
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView doLogin(
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			ModelAndView mv) {
		//メールアドレス、パスワードが空欄の時にエラーとする
		if (email == null || email.length() == 0) {
			if (password == null || password.length() == 0) {
				mv.addObject("message1", "メールアドレスを入力してください");
				mv.addObject("message2", "パスワードを入力してください");
				mv.setViewName("home");
				return mv;
			}
			//メールアドレスが空欄の時にエラーとする
			else {
				mv.addObject("message1", "メールアドレスを入力してください");
				mv.setViewName("home");
				return mv;
			}
		}
		//パスワードが空欄の時にエラーとする
		if (password == null || password.length() == 0) {
			mv.addObject("message2", "パスワードを入力してください");
			mv.addObject("email", email);
			mv.setViewName("home");
			return mv;
		}

		//ログイン処理

		List<User> users = userRepository.findByEmail(email);
		// メールアドレスが存在したら
		if (users.size() > 0) {
			// リストの1件目をログインユーザとして取得する
			User user = users.get(0);

			String _email = user.getEmail();
			String _password = user.getPassword();

			if (email.equals(_email) && password.equals(_password)) {
				//ログイン成功
				String name = user.getName();

				session.setAttribute("name", name);
				session.setAttribute("user", user);

				mv.addObject("name", name);
				mv.addObject("user", user);

				mv.setViewName("main");
			} else { //メールアドレスとパスワードが不一致 ログインNG
				mv.addObject("RESULT", "メールアドレスとパスワードが一致しませんでした。");
				mv.setViewName("home");
			}

		} else if (users.size() == 0) {
			// メールアドレスが見つからなかった場合
			// エラーメッセージをセット
			mv.addObject("RESULT", "入力されたメールアドレスは登録されていません");
			mv.setViewName("home");
		}

		return mv;
	}

	//新規会員登録ページへ移動
	@RequestMapping(value = "/new")
	public ModelAndView addUser(ModelAndView mv) {
		mv.setViewName("new");
		return mv;
	}

	//会員登録実行
	@PostMapping(value = "/new")
	public ModelAndView doAddUser(ModelAndView mv,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "tel", defaultValue = "") String tel,
			@RequestParam(name = "password", defaultValue = "") String password) {

		//サニタイジング
		Sanitizing sa = new Sanitizing();
		sa.convert(name);
		sa.convert(email);
		sa.convert(tel);
		sa.convert(password);

		List<User> users = userRepository.findByEmail(email);

		//未入力の項目がある時
		if (name == "" || name.length() == 0 || email == "" || email.length() == 0 || tel == "" || tel.length() == 0
				|| password == "" || password.length() == 0) {
			mv.addObject("RESULT", "未入力の項目があります");
			mv.setViewName("new");
		}
		// メールアドレスがすでに登録されている時
		else if (users.size() > 0) {
			// エラーメッセージをセット
			mv.addObject("RESULT", "メールアドレスがすでに登録されています");
			mv.setViewName("new");
		}

		//登録が成功した時
		else {
			//登録するUserエンティティのインスタンスを生成
			User user = new User(name, email, password, tel);

			//UserエンティティをUsersテーブルに登録
			userRepository.saveAndFlush(user);

			mv.addObject("RESULT", name + "さん、登録が完了しました。ログイン画面に戻ってログインしてください。");
			mv.setViewName("new");
		}
		return mv;
	}


}
