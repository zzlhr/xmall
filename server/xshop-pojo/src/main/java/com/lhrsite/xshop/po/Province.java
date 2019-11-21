package com.lhrsite.xshop.po;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Province implements Serializable {

    @Id
    /* null */
    private Integer id;
    /* null */
    private String name;
    /* null */
    @JsonProperty("value")
    private String provinceId;

}
