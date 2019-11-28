package com.lhrsite.xshop.vo;


import com.querydsl.core.Tuple;
import lombok.Data;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class UserVO {

    /** 用户id */
    private Integer uid;
    /** 用户名 */
    private String username;
    /** 用户手机号 */
    private String phone;
    /** 用户邮箱 */
    private String email;

    /** 用户登录令牌 */
    private String token;
    /** 用户头像 */
    private String header;

    /** 用户权限组 */
    private Integer authGroup;
    /** 用户权限组名称 */
    private String authGroupName;

    /** 用户状态 */
    private Integer status;

    /** 用户创建时间 */
    private String createTime;
    /** 用户更新时间 */
    private String updateTime;




}
