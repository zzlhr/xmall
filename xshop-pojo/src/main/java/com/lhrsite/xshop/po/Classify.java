package com.lhrsite.xshop.po;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Classify implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clId;
    private String clName;
    private Integer clGrade;
    private Integer clFid;
    private Integer clSerial;
    private Integer eid;
    private String picture;
}
