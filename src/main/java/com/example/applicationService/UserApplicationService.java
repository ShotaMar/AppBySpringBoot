package com.example.applicationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.domainUser.model.MUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ResourceLoader resourceLoader;

    /** ファイル保存先 */
    private String filePath = "C¥¥work";

    /** パス区切り文字 */
    private static final String SEPARATOR = File.separator;

    /** 性別のMapを生成する */
    public Map<String,Integer> getGenderMap(Locale locale){
        Map<String,Integer> genderMap = new LinkedHashMap<>();
        String male = messageSource.getMessage("male", null, locale);
        String female = messageSource.getMessage("female",null, locale);
        genderMap.put(male, 1);
        genderMap.put(female, 2);

        return genderMap;
    }

    /** ユーザリストをCSVに保存する */
    public void saveUserCSV(List<MUser> userList, String fileName) throws IOException{
        //CSV文字列を作成
        StringBuilder sb = new StringBuilder();
        for(MUser user : userList){
            sb.append(user.toCsv());
        }
        //ファイル保存先パス作成
        Path path = Paths.get(filePath + SEPARATOR + fileName);
        //byte配列作成
        byte[] bytes = sb.toString().getBytes();
        //ファイル書き込み
        Files.write(path, bytes);
    }

    /** CSVファイル取得 */
    public byte[] getCsv(String fileName) throws IOException{
        //パス
        String path = "file:" + filePath + SEPARATOR + fileName;
        //ファイル取得
        Resource resource = resourceLoader.getResource(path);
        File file = resource.getFile();
        //byte配列取得
        return Files.readAllBytes(file.toPath());
    }
}
