package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {
	List<CheckIn> findByReservecode(Integer code);
	List<CheckIn> findByUserscode(Integer userscode);
}
