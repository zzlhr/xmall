package com.lhrsite.xshop.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Town implements Serializable{

    @Id
    /* null */
    private Integer id;
    /* null */
    private String name;
    /* null */
    @JsonProperty("value")
    private String townId;
    /* null */
    @JsonProperty("parent")
    private String countryId;

}
