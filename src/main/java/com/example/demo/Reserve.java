package com.example.demo;

import java.sql.Date;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reserve")
public class Reserve {
	private static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;

	@Column(name="userscode")
	private Integer userscode;


	private Date ymd;

	@Column(name="reservedate")
	private String reservedate;

	@Column(name="start")
	private String start;

	@Column(name="finish")
	private String finish;

	@Column(name="room")
	private String room;

	@Column(name="seat")
	private String seat;

	public Reserve() {

	}

	//登録用
	public Reserve(Integer userscode,Date ymd, String reservedate, String start, String finish, String room, String seat) {
		this.userscode = userscode;
		this.ymd = ymd;
		this.reservedate = reservedate;
		this.start = start;
		this.finish = finish;
		this.room = room;
		this.seat = seat;
	}
	public Reserve(Integer userscode,String reservedate, String start, String finish, String room, String seat) {
		this.userscode = userscode;

		this.reservedate = reservedate;
		this.start = start;
		this.finish = finish;
		this.room = room;
		this.seat = seat;
	}

	public Reserve(Integer code, Integer userscode,Date ymd,String reservedate, String start, String finish, String room, String seat ) {
		this.code = code;
		this.userscode = userscode;
		this.ymd = ymd;
		this.reservedate = reservedate;
		this.start = start;
		this.finish = finish;
		this.room = room;
		this.seat = seat;
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

	public Date getYmd() {
		return ymd;
	}

	public void setYmd(Date ymd) {
		this.ymd = ymd;
	}

	public String getDate() {
		return reservedate;
	}

	public void setDate(String date) {
		this.reservedate = date;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getReservedate() {
		return reservedate;
	}

	public void setReservedate(String reservedate) {
		this.reservedate = reservedate;
	}

}
