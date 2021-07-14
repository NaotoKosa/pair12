package com.example.demo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="message")
public class Message {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;

	@Column(name="userscode")
	private Integer userscode;

	@Column(name="name")
	private String name;

	private Date ymd;


	@Column(name="message")
	private String message;

	public Message() {

	}

	public Message(Integer userscode, String name, Date ymd, String message) {

		this.userscode = userscode;
		this.name = name;
		this.ymd = ymd;
		this.message = message;
	}


	public Message(Integer code, Integer userscode, String name, Date ymd, String message) {

		this.code = code;
		this.userscode = userscode;
		this.name = name;
		this.ymd = ymd;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getUserscode() {
		return userscode;
	}

	public void setUserscode(Integer userscode) {
		this.userscode = userscode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getYmd() {
		return ymd;
	}

	public void setYmd(Date ymd) {
		this.ymd = ymd;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}




}
