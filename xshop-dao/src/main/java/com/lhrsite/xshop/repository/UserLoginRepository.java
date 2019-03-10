package com.lhrsite.xshop.repository;

import com.lhrsite.xshop.po.UserLogin;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository
        extends BaseRepository<UserLogin, String> {


    UserLogin findByUserToken(String userToken);



}
