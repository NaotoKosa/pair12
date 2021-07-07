package com.example.demo;

import java.time.LocalDateTime;
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

	@Column(name="users_code")
	private Integer userscode;

	private LocalDateTime time;
	private String reservetime;

	@Column(name="room_code")
	private Integer roomcode;

	private String seat;

	public Reserve() {

	}

	public Reserve(Integer code, Integer userscode, String time, String reservetime, Integer roomcode, String seat ) {
		this.code = code;
		this.userscode = userscode;
		this.time = LocalDateTime.now();
		this.reservetime = reservetime;
		this.roomcode = roomcode;
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

	public String getTime() {
		return time.format(fmt);
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getReservetime() {
		return reservetime;
	}

	public void setReservetime(String reservetime) {
		this.reservetime = reservetime;
	}

	public Integer getRoomcode() {
		return roomcode;
	}

	public void setRoomcode(Integer roomcode) {
		this.roomcode = roomcode;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}


}
