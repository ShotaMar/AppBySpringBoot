package com.example.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.domainUser.model.MUser;
import com.example.domainUser.service.UserService;
import com.example.form.GroupOrder;
import com.example.form.SignupForm;
import com.example.form.UserDetailForm;
import com.example.form.UserListForm;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    /** ユーザを検索 */
    @GetMapping("/get/list")
    public List<MUser> getUserList(UserListForm form){
        
        //formをMUserクラスに変換
        MUser user = modelMapper.map(form, MUser.class);

        //ユーザ一覧取得
        List<MUser> userList = userService.getUsers(user);
        return userList;
    }

    /** ユーザを登録 */
    @PostMapping("/signup/rest")
    public RestResult postSignup(@Validated(GroupOrder.class) SignupForm form, 
                                    BindingResult bindingResult, Locale locale){
        //入力チェック結果
        if(bindingResult.hasErrors()){
            //チェックNGの場合
            Map<String,String> errors = new HashMap<>();

            //エラーメッセージ取得
            for(FieldError error : bindingResult.getFieldErrors()){
                String message = messageSource.getMessage(error, locale);
                errors.put(error.getField(), message);
            }

            //エラー結果の返却
            return new RestResult(90, errors);
        }
        //formをMUser型に変換
        MUser user = modelMapper.map(form, MUser.class);
        //ユーザ登録
        userService.signup(user);
        //結果の返却
        return new RestResult(0, null);

    }

    /** ユーザを更新 */
    @PutMapping("/update")
    public int updateUser(UserDetailForm form){
        //ユーザを更新
        userService.updateUserOne(form.getUserId(), form.getPassword(), form.getUserName());

        return 0;
    }

    /** ユーザを削除 */
    @DeleteMapping("/delete")
    public int deleteUser(UserDetailForm form){
        userService.deleteUserOne(form.getUserId());

        return 0;
    }
}
