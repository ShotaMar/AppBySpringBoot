package com.example.domainUser.service.impl;

import java.util.List;
import java.util.Optional;

import com.example.domainUser.model.MUser;
import com.example.domainUser.service.UserService;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Primary
public class UserServiceImpl2 implements UserService{
    
    @Autowired
    private UserRepository repository;


    @Autowired
    private PasswordEncoder encoder;

    /** ユーザ登録 */
    @Transactional
    @Override
    public void signup(MUser user){
        //存在チェック
        boolean exists = repository.existsById(user.getUserId());
        if(exists){
            throw new DataAccessException("ユーザが既に存在します"){};
        }

        user.setDepartmentId(1);
        user.setRole("ROLE_GENERAL");

        //パスワードを暗号化
        String rawPassword = user.getPassword();
        user.setPassword(encoder.encode(rawPassword));

        //insert
        repository.save(user);
    }

    /** ユーザ取得 */
    @Override
    public List<MUser> getUsers(MUser user){
        // mybatis
        // return repository.findAll();

        //Spring Data JPA
        ExampleMatcher matcher = ExampleMatcher.matching() //and条件の指定
                                                .withStringMatcher(StringMatcher.CONTAINING) //Like旬
                                                .withIgnoreCase(); //大文字・小文字の両方

        return repository.findAll(Example.of(user, matcher));
    }

    /** ユーザ取得（１件） */
    @Override
    public MUser getUserOne(String userId){
        Optional<MUser> option = repository.findById(userId);
        MUser user = option.orElse(null);
        return user;
    }

    /** ユーザ削除 */
    @Transactional
    @Override
    public void deleteUserOne(String userId){
        repository.deleteById(userId);
    }

    /** ユーザ更新（１件） */
    @Transactional
    @Override
    public void updateUserOne(String userId, String password, String userName) {
        //do nothing on mybatis

        //springData jpa
        //パスワード暗号化
        String encryptPassword = encoder.encode(password);
        //ユーザ更新
        repository.updateUser(userId, encryptPassword, userName);
    }
    
    /** ログインユーザ取得 */
    @Override
    public MUser getLoginUser(String userId) {
        //on mybatis
        // Optional<MUser> option = repository.findById(userId);
        // MUser user = option.orElse(null);
        // return user;

        //springData jpa
        return repository.findLoginUser(userId);   

    }
}
