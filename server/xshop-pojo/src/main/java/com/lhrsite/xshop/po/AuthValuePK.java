package com.lhrsite.xshop.po;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class AuthValuePK implements Serializable {
    @Id
    private Integer groupId;

    @Id
    private Integer menuId;
}
