package com.txst.restapi.controller;


import com.mysql.cj.util.StringUtils;
import com.txst.restapi.model.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public User user(@RequestParam(value = "id", defaultValue = "1") int id){
        return new User(id);
    }

    @GetMapping("/user/create")
    public User createUser(@RequestParam(value = "name") String userName, @RequestParam(value="email") String email, @RequestParam(value = "password") String password){
        User user = new User(-1);
        if(StringUtils.isEmptyOrWhitespaceOnly(userName)
                ||(StringUtils.isEmptyOrWhitespaceOnly(email))
                ||(StringUtils.isEmptyOrWhitespaceOnly(password))){
            return user;
        }
        user.createUser(userName,email,password);
        return user;
    }

    @GetMapping("/user/validate")
    public User validateUser(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password){
        User user = new User(-1);
        if(StringUtils.isEmptyOrWhitespaceOnly(email)
                || StringUtils.isEmptyOrWhitespaceOnly(password)){
            user.setErrorMessage("Missing required fields email/password");
            return user;
        }

        user.validateLogin(email, password);
        return user;
    }
}
