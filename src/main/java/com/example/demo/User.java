package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int code;
	private String name;
	private String email;
	private String password;
	private String tel;

	//コンストラクタ
	public User() {

	}

	//会員登録用
	public User(String name, String email, String password, String tel) {

		this.name = name;
		this.email = email;
		this.password = password;
		this.tel = tel;
	}

	public User(int code, String name, String email, String password, String tel) {
		super();
		this.code = code;
		this.name = name;
		this.email = email;
		this.password = password;
		this.tel = tel;
	}

	//ゲッタ・セッタ
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}


}
