package com.lhrsite.xshop.po;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class City implements Serializable {


    @Id
    private Integer id;
    private String name;
    @JsonProperty("value")
    private String cityId;
    @JsonProperty("parent")
    private String provinceId;

}
