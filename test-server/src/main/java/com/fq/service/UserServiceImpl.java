package com.fq.service;

import cn.hutool.core.util.IdUtil;
import com.fq.api.User;
import com.fq.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(Long id) {
        return User.builder()
                .id(id)
                .name(IdUtil.fastSimpleUUID())
                .build();
    }
}
