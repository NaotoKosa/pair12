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
@Table(name="checkout")
public class CheckOut {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer code;
	@Column(name="userscode")
	private Integer userscode;
	@Column(name="reservecode")
	private Integer reservecode;
	@Column(name="checkout")
	private LocalDateTime finish;

	public CheckOut() {

	}

	public CheckOut(Integer userscode, Integer reservecode, LocalDateTime finish) {
		this.userscode = userscode;
		this.reservecode = reservecode;
		this.finish = finish;

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

	public LocalDateTime getfinish() {
		return finish;
	}

	public void setFinish(LocalDateTime finish) {
		this.finish = finish;
	}

	public String getFinishView() {
		return finish.format(DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm:ss"));
	}



}
