package com.example.repository;

import com.example.domainUser.model.MUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<MUser,String>{
    
    /** ログインユーザ検索 */
    @Query("select user" + " from MUser user"+ " where userId = :userId")
    public MUser findLoginUser(@Param("userId")String userId);

    /** ユーザ更新 */
    @Modifying
    @Query("update MUser" + " set" + " password = :password" 
                + ", userName = :userName" + " where" + " userId = :userId")
    public Integer updateUser(@Param("userId")String userId,
                                @Param("password")String password,
                                @Param("userName")String userName);
}
