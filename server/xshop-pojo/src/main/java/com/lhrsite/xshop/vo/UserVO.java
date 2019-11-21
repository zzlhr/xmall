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

    /** 企业代号 */
    private Integer enterprise;
    /** 企业名称 */
    private String enterpriseName;

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


    public static void tupleToUserVO(Tuple tuple, List<UserVO> userVOS){
        UserVO userVO = new UserVO();
        userVO.setUid(tuple.get(0, Integer.class));
        userVO.setUsername(tuple.get(1, String.class));
        userVO.setPhone(tuple.get(2, String.class));
        userVO.setEmail(tuple.get(3, String.class));
        userVO.setEnterpriseName(tuple.get(5, String.class));
        userVO.setHeader(tuple.get(6, String.class));
        userVO.setCreateTime(tuple.get(7, Timestamp.class).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        userVO.setUpdateTime(tuple.get(8, Timestamp.class).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        userVO.setStatus(tuple.get(9, Integer.class));
        userVOS.add(userVO);
    }


}
