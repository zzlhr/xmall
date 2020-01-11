package com.lhrsite.xshop.core.exception;


/**
 * 所有失败返回都会抛出该异常类，如果一个请求出现该类异常即视为成功
 * 所有为成功的逻辑都需要抛出该异常。
 *
 * 改异常的所有内容来源于ErrEumn枚举中的错误。
 */
public class XShopException extends Exception {

    private ErrEumn errEumn;

    public ErrEumn getErrEumn() {
        return errEumn;
    }

    public XShopException(ErrEumn errEumn){
        super("XShopException errCode=" + errEumn.getCode()
                + "errMsg=" + errEumn.getMessage());
        this.errEumn = errEumn;
    }
}
