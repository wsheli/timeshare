package com.timeshare.controller;

import com.timeshare.domain.UserInfo;
import com.timeshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by user on 2016/6/23.
 */
public class BaseController {

    @Autowired
    UserService userService;

    public UserInfo getCurrentUser(String userId){
        UserInfo userInfo = userService.findUserByUserId("admin");
        return userInfo;
    }
}