package com.lhrsite.xshop.po;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class Country implements Serializable{

    @Id
    /* null */
    private Integer id;
    /* null */
    private String name;
    /* null */
    @JsonProperty("value")
    private String countryId;
    /* null */
    @JsonProperty("parent")
    private String cityId;

}
