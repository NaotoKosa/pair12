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
@Table(name="checkin")
public class CheckIn {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer code;
	@Column(name="userscode")
	private Integer userscode;
	@Column(name="reservecode")
	private Integer reservecode;
	@Column(name="checkin")
	private LocalDateTime start;

	public CheckIn() {

	}

	public CheckIn(Integer userscode, Integer reservecode , LocalDateTime start) {
		this.userscode = userscode;
		this.reservecode = reservecode;
		this.start = start;
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

	public Integer getReservecode() {
		return reservecode;
	}

	public void setReservecode(Integer reservecode) {
		this.reservecode = reservecode;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public String getStartView() {
		return start.format(DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm:ss"));
	}

}
