package com.github.tbquyen.password;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PasswordDAO {
	@Update("UPDATE accounts SET password=#{password}, status='1' WHERE username = #{username}")
	public int updatePassword(@Param("username") String username, @Param("password") String password);
}
