package com.lhrsite.xshop.repository;


import com.lhrsite.xshop.po.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
        extends BaseRepository<User, Integer> {


    User findByPhone(String phone);

}
