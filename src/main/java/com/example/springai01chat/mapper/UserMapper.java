package com.example.springai01chat.mapper;

import com.example.springai01chat.dto.RegisterUserDto;
import com.example.springai01chat.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    UserVo findUserByUserName(@Param("userName") String userName);

    boolean insertUser(@Param("registerUserDto") RegisterUserDto registerUserDto);
}
