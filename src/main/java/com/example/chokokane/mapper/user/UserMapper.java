package com.example.chokokane.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.chokokane.model.user.User;

@Mapper
public interface UserMapper {

  void save(@Param("user") User user);

  User findByEmail(@Param("email") String email);

  User findById(@Param("userId") Long userId);

  int deleteMe(@Param("userId") Long userId);
}
