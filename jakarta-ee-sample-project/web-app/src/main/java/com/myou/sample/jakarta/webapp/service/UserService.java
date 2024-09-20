package com.myou.sample.jakarta.webapp.service;

import com.myou.sample.jakarta.webapp.entity.UserEntity;
import com.myou.sample.jakarta.webapp.mapper.UserMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.mybatis.cdi.Transactional;


import java.util.List;

@RequestScoped
@Transactional
public class UserService {

    @Inject
    private UserMapper userMapper;

    public List<UserEntity> getAllEntities(){
        return userMapper.selectAll();
//    return null;
    }


}
