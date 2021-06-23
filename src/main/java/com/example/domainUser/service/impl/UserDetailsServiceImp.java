package com.example.domainUser.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.domainUser.model.MUser;
import com.example.domainUser.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    
    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) 
                            throws UsernameNotFoundException{
        
        //ユーザ情報取得
        MUser loginUser = service.getLoginUser(username);
        //ユーザが存在しない場合
        if(loginUser == null){
            throw new UsernameNotFoundException("user not found");
        }

        //権限List作成
        GrantedAuthority authority = new SimpleGrantedAuthority(loginUser.getRole());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);

        //UserDetails作成
        UserDetails userDetails = 
            (UserDetails) new User(loginUser.getUserId(),loginUser.getPassword(),authorities);

        return userDetails;
    }
}
