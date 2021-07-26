package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Integer>{

	List<Reserve> findByUserscode(Integer userscode);
	List<Reserve> findByUserscodeOrderByCodeDesc(Integer userscode);
	List<Reserve> findByUserscodeAndReservedate(int userscode, String str);
	List<Reserve> findByRoom(String room);
	List<Reserve> findByCode(Integer code);
}
