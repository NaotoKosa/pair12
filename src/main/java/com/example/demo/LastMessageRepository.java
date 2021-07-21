package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LastMessageRepository extends JpaRepository<LastMessage, Integer> {
	List<LastMessage> findByUserscode(Integer userscode);

}
