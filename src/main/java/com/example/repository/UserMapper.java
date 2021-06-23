package com.example.repository;


import java.util.List;

import com.example.domainUser.model.MUser;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMapper {

    /** ユーザ登録 */
    public int insertOne(MUser user);
    /** ユーザs取得 */
    public List<MUser> findMany(MUser user);
    /** ユーザ取得(1件) */
    public MUser findOne(String userId);
    /** ユーザ更新(1件) */
    public void updateOne(@Param("userId")String userId,
                            @Param("password")String password,@Param("userName")String userName);
    /** ユーザ削除(1件) */
    public int deleteOne(@Param("userId")String userId);

    /** ログインユーザ取得 */
    public MUser findLoginUser(String userId);

}
