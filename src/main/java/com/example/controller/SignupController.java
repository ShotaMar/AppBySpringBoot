package com.example.controller;

import java.util.Locale;
import java.util.Map;

import com.example.applicationService.UserApplicationService;
import com.example.domainUser.model.MUser;
import com.example.domainUser.service.UserService;
import com.example.form.GroupOrder;
import com.example.form.SignupForm;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class SignupController {
    
    @Autowired
    private UserApplicationService userApplicationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/signup")
    public String getSignup(Model model, Locale locale,
                                @ModelAttribute SignupForm form){
        Map<String,Integer> genderMap = userApplicationService.getGenderMap(locale);
        model.addAttribute("genderMap", genderMap);

        return "user/signup";
    }

    /** ユーザ登録処理 
     * @param
     * フォームのバインド結果とか
     * @return
     * 登録画面 or ログイン画面
    */
    @PostMapping("/signup")
    public String postSignup(Model model, Locale locale, 
                                @ModelAttribute @Validated(GroupOrder.class) SignupForm form, BindingResult bindingResult){
        
        //入力チェック結果の判定
        if(bindingResult.hasErrors()){
            //NGの場合 ユーザ登録画面へ戻す
            return getSignup(model, locale, form);
        }
        log.info(form.toString());

        //formをMuserクラスに変換
        MUser user = modelMapper.map(form, MUser.class);
        //ユーザ登録
        userService.signup(user);

        return "redirect:/login";
    }

    /**データベース関連の例外処理 */
    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(DataAccessException e, Model model){
        
        //空文字をセット
        model.addAttribute("error", "");

        //メッセージをModelに登録
        model.addAttribute("message", "SignupControllerで例外が発生しました");

       //HTTPのエラーコード(500)をModelに登録
       model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
       
       return "error";
    }

    /**その他の例外処理 */
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model){
        
        //空文字をセット
        model.addAttribute("error", "");

        //メッセージをModelに登録
        model.addAttribute("message", "SignupControllerで例外が発生しました");

        //HTTPのエラーコード(500)をModelに登録
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "error";
    }
}
