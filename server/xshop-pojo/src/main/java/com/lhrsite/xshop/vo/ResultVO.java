package com.lhrsite.xshop.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO {

    private Integer code;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)//转json时data为null时不系列化
    private Object data;


    public static ResultVO create(Object data) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        resultVO.setData(data);
        return resultVO;
    }

}
