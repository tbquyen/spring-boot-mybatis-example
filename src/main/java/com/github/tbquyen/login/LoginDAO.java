package com.github.tbquyen.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginDAO {
  @Select("SELECT * FROM accounts WHERE username = #{username}")
  @Results(value = {
      @Result(property = "username", column = "username"),
      @Result(property = "password", column = "password"),
      @Result(property = "role", column = "role"),
      @Result(property = "status", column = "status"),
    })
  public UserDetailsImpl loadUserByUsername(String userId);
}
