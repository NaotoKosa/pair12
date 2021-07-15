package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckOutRepository extends JpaRepository<CheckOut, Integer> {
	List<CheckOut> findByReservecode(Integer code);
	List<CheckOut> findByUserscode(Integer userscode);
}
