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

	private Date ymd;

	@Column(name="message_to")
	private String to;

	@Column(name="message_from")
	private String from;

	@Column(name="userscode")
	private Integer userscode;

	@Column(name="email")
	private String email;

	@Column(name="message")
	private String message;

	public Message() {

	}

	public Message(Date ymd, String to, String from, Integer userscode, String email, String message) {
		this.ymd = ymd;
		this.to = to;
		this.from = from;
		this.userscode = userscode;
		this.email = email;
		this.message = message;
	}

	public Message(Integer code, Date ymd, String to, String from, Integer userscode, String email, String message) {
		this.code = code;
		this.ymd = ymd;
		this.to = to;
		this.from = from;
		this.userscode = userscode;
		this.email = email;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Date getYmd() {
		return ymd;
	}

	public void setYmd(Date ymd) {
		this.ymd = ymd;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Integer getUserscode() {
		return userscode;
	}

	public void setUserscode(Integer userscode) {
		this.userscode = userscode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
