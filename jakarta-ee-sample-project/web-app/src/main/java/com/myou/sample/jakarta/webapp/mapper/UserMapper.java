package com.myou.sample.jakarta.webapp.mapper;

import com.myou.sample.jakarta.webapp.entity.UserEntity;
import org.apache.ibatis.annotations.Select;
import org.mybatis.cdi.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user_table")
    List<UserEntity> selectAll();

}
