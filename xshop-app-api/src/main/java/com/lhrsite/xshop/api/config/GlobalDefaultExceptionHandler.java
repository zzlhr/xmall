package com.lhrsite.xshop.api.config;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Set;

/**
 * 全局异常捕获
 */
@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalDefaultExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = XShopException.class)
    public ResultVO defaultErrorHandler(XShopException e)
            throws IOException {

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(e.getErrEumn().getCode());
        resultVO.setMsg(e.getErrEumn().getMessage());
        return resultVO;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultVO handleServiceException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        log.error("【参数错误】e={}", message);
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(-1);
        resultVO.setMsg(message);
        return resultVO;
    }
}
