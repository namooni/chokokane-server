package com.example.chokokane.repository.user;

import org.springframework.stereotype.Repository;

import com.example.chokokane.mapper.user.UserMapper;
import com.example.chokokane.model.user.User;

@Repository
public class MyBatisUserRepository implements UserRepository {

  private UserMapper userMapper;

  public MyBatisUserRepository(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public void save(User user) {
    userMapper.save(user);
  }

  @Override
  public User findByEmail(String email) {
    return userMapper.findByEmail(email);
  }

  @Override
  public User findById(Long userId) {
    return userMapper.findById(userId);
  }

  @Override
  public int deleteMe(Long userId) {
    return userMapper.deleteMe(userId);
  }
}
