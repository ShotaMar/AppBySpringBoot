package com.example.controller;

import java.io.IOException;
import java.util.List;

import com.example.applicationService.UserApplicationService;
import com.example.domainUser.model.MUser;
import com.example.domainUser.service.UserService;
import com.example.form.UserListForm;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/user")
public class UserListController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserApplicationService appService;

    /** ユーザ一覧画面を表示 */
    @GetMapping("/list")
    public String getUserList(@ModelAttribute UserListForm form, Model model){
        
        MUser user = modelMapper.map(form,MUser.class);
        List<MUser> userList = userService.getUsers(user);
        model.addAttribute("userList",userList);

        return "user/list";
    }

    /** ユーザ検索処理 */
    @PostMapping("/list")
    public String postUserList(@ModelAttribute UserListForm form, Model model){

        MUser user = modelMapper.map(form, MUser.class);
        List<MUser> userList = userService.getUsers(user);
        model.addAttribute("userList", userList);

        return "user/list";
    }

    /** ユーザ一覧ダウンロード処理 */
    @PostMapping("/list/download")
    public ResponseEntity<byte[]> downloadUserList(@ModelAttribute UserListForm form) 
                                                                    throws IOException {
        //formをMUserクラスに変換
        MUser user = modelMapper.map(form, MUser.class);
        //ユーザ検索
        List<MUser> userList = userService.getUsers(user);
        //CSVファイル保存
        String fileName = "user.csv";
        appService.saveUserCSV(userList, fileName);
        //CSVファイル取得
        byte[] bytes = appService.getCsv(fileName);

        HttpHeaders header = new HttpHeaders();
        //HTTPヘッダーの設定
        header.add("Content-Type", MediaType.ALL_VALUE + "; charset=utf-8");
        header.setContentDispositionFormData("fileName", fileName);

        return new ResponseEntity<>(bytes,header,HttpStatus.OK);
    }
    

}
