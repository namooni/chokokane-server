package com.example.chokokane.repository.user;

import com.example.chokokane.model.user.User;

public interface UserRepository {

  void save(User user);

  User findByEmail(String email);

  User findById(Long userId);

  int deleteMe(Long userId);
}
