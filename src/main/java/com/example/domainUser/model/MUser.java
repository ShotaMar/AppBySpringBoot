package com.example.domainUser.model;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name="m_user")
public class MUser {
    @Id
    private String userId;
    private String password;
    private String userName;
    private Date birthday;
    private Integer age;
    private Integer gender;
    private Integer departmentId;
    private String role;
    @Transient
    private Department department;
    @Transient
    private List<Salary> salaryList;

    /** CSV文字列の作成 */
    public String toCsv(){
        String genderStr = null;
        genderStr = gender == 1? "男性":"女性";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String csv = userId + "," + userName + "," + sdf.format(birthday)
                            + "," + age + "," + genderStr + "\r\n";

        return csv;
    }

}
