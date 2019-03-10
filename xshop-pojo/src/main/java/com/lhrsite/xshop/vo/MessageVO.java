package com.lhrsite.xshop.vo;


import com.lhrsite.xshop.po.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class MessageVO extends Message implements Serializable {


    private String sendUserName;


}
