package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 企业实体类
 */
@Entity
@Data
public class Enterprise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eid;
    private String epName;
    private String epShortName;
    private String epLink;
    private String epRemark;
    private Integer epType;
    private Integer epStatus;
    @Column(insertable = false, updatable = false)
    private Timestamp createTime;
    @Column(insertable = false, updatable = false)
    private Timestamp updateTime;


}
