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

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	LastMessageRepository lastmessageRepository;

	@Autowired
	MasterLastMessageRepository masterlastmessageRepository;


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

	//管理者ログイン
				if(email.equals("master@aaa.com") && password.equals("master")) {
					messageCheckMaster();
					mv.setViewName("masterMain");

				}else {
		//ログイン処理
		List<User> users = userRepository.findByEmail(email);
		// メールアドレスが存在したら
		if (users.size() > 0) {
			// リストの1件目をログインユーザとして取得する
			User user = users.get(0);

			String _email = user.getEmail();
			String _password = user.getPassword();

			if (email.equals(_email) && password.equals(_password)) {
				//ユーザログイン成功
				String name = user.getName();

				session.setAttribute("name", name);
				session.setAttribute("user", user);

				mv.addObject("name", name);
				mv.addObject("user", user);

				int userscode = user.getCode();

				//新着メッセージを調べる
				messageCheck(userscode);

				mv.setViewName("main");

			} else { //メールアドレスとパスワードが不一致 ログインNG
				mv.addObject("RESULT", "メールアドレスとパスワードが一致しませんでした。");
				mv.setViewName("home");
			}

		} else if (users.size() == 0) {
			// メールアドレスが見つからなかった場合
			// エラーメッセージをセット
			mv.addObject("RESULT", "メールアドレスとパスワードが一致しませんでした。");
			mv.setViewName("home");
		}
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

	//ユーザ情報の確認
	@RequestMapping(value = "/userInfo")
	public ModelAndView userInfo(ModelAndView mv) {
		User user = (User) session.getAttribute("user");
		int code = user.getCode();

		List<User> list = userRepository.findByCode(code);
		mv.addObject("user",list);
		mv.setViewName("userInfo");
		return mv;
	}
	//ユーザ情報の変更
	@RequestMapping(value = "/modify")
	public ModelAndView modify(ModelAndView mv) {
		User user = (User) session.getAttribute("user");

		mv.addObject("user",user);
		mv.setViewName("userInfoModify");
		return mv;
	}
	//ユーザ情報の変更実施
	@PostMapping(value = "/modify")
	public ModelAndView domodify(ModelAndView mv,
			@RequestParam(name = "code", defaultValue = "") int code,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "tel", defaultValue = "") String tel,
			@RequestParam(name = "password", defaultValue = "") String password) {

		List<User> users = userRepository.findByEmail(email);
		User user = (User) session.getAttribute("user");
		String _email = user.getEmail();//今の登録メールアドレス

		//未入力の項目がある時
			if (name == "" || name.length() == 0 || email == "" || email.length() == 0 || tel == "" || tel.length() == 0
					|| password == "" || password.length() == 0) {

				mv.addObject("user",user);
				mv.addObject("RESULT", "未入力の項目があります");
				mv.setViewName("userInfoModify");
			}
		//メールアドレスが他のアカウントですでに登録されている時
			else if(!email.equals(_email) && users.size() > 0) {

				mv.addObject("user",user);
				mv.addObject("RESULT", "メールアドレスが他のアカウントですでに登録されています");
				mv.setViewName("userInfoModify");
			}
			else {
		User _user = new User(code,name,email,password,tel);

		userRepository.saveAndFlush(_user);//変更

		mv.addObject("user",user);
		mv.addObject("RESULT","ユーザ情報を変更しました。");
		mv.setViewName("userInfoModify");
			}
		return mv;
	}

	@RequestMapping(value = "/main")
	public ModelAndView main(ModelAndView mv) {
		//新着メッセージを調べる
		User user = (User) session.getAttribute("user");
		int userscode = user.getCode();
		messageCheck(userscode);
		mv.setViewName("main");
		return mv;
	}

	//新着メッセージが来ているかチェック
	public void messageCheck(int userscode) {
		boolean n = false;

		//最後に送られてきたメッセージのCodeを取得
		List<Message> list = messageRepository.findByUserscodeOrderByCodeAsc(userscode);
		Message last = list.get(list.size() - 1);
		int lastCode = last.getCode();

		//前回見たメッセージ一覧の最後のCodeを取得
		List<LastMessage> list2 = lastmessageRepository.findByUserscode(userscode);
		int s = list2.size();
			if(s!=0) {
				LastMessage _last = list2.get(0);
				int _lastCode = _last.getLast();
				System.out.println(_lastCode);
				//見ていないメッセージがあったとき
				if(lastCode > _lastCode) {
					n = true;
				}
			}
		session.setAttribute("n", n);
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
				System.out.println(_lastCode);
				//見ていないメッセージがあったとき
				if(lastCode > _lastCode) {
					n = true;
				}
			}
		session.setAttribute("n", n);
	}


}