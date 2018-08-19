package com.hans.fm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hans.fm.model.User;

public interface FriendRepository extends JpaRepository<User, String> {
	
	User findByEmail(String email);
	
}
