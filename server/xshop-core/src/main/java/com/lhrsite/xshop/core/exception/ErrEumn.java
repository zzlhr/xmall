package com.lhrsite.xshop.core.exception;

public enum ErrEumn {
    USER_NO_EXIST(-10000, "用户不存在"),
    ADD_USER_ERR(-10001, "添加用户失败"),
    UPDATE_USER_PARAMS_ERR(-10002, "修改用户-非法参数"),
    UPDATE_USER_ERR(-10003, "修改用户-非法参数"),
    DELECT_USER_ERR(-10004, "删除用户失败"),
    LOGIN_ERR(-10005, "用户名或密码错误"),
    EXPIRE_TOKEN(-10006, "身份认证过期,请重新登录"),
    PARAM_IS_NULL(-10007, "参数为空"),

    ADD_USER_UID_IS_NULL(-10008, "uid不能为空！"),
    ADD_USER_PASSWORD_IS_NULL(-10008, "密码不能为空！"),
    ADD_USER_USERNAME_IS_NULL(-10009, "用户名不能为空！"),
    ADD_USER_PHONE_IS_NULL(-10010, "手机号不能为空！"),
    ADD_USER_STATUS_IS_NULL(-10011, "状态不能为空！"),
    ADD_USER_ENTERPRISE_IS_NULL(-10012, "企业不能为空！"),
    ADD_USER_IS_NULL(-10012, "添加用户对象为空！"),


    AUTH_GROUP_NULL(-10015, "权限组对象为空！"),
    ENTERPRISE_NULL(-10016, "企业id为空！"),
    UPDATEUSER_NULL(-10017, "更新用户为空！"),
    AUTH_GROUP_NAME_NULL(-10018, "权限组名称为空！"),

    EDIT_AUTH_GROUP_ERROR(-10013, "编辑权限组失败！"),


    TOKEN_IS_NULL(-10014, "token为空！"),


    ADD_ENTERPRISE_NULL(-10019, "企业对象不能为空！"),
    ADD_ENTERPRISE_SHORT_NAME_NULL(-10020, "企业简称不能为空！"),
    ADD_ENTERPRISE_NAME_NULL(-10021, "企业名称不能为空！"),


    ADD_USER_PHONE_EXIST(-10022, "手机号已注册！"),


    UPLOAD_ERROR_FILE_NULL(-100023, "文件不能为空"),
    UPLOAD_ERROR(-100023, "上传文件失败"),


    CLASSIFY_IS_NOTEXIST(-100024, "分类不存在！"),

    BUY_CAR_IS_NULL(-100025, "购物车空空如也！"),

    CLASSIFY_IS_EXIST(-100026, "分类已存在！"),

    ADDRESS_NOT_EXIST(-100027, "该地址不存在！"),
    ONLY_DELETE_YOUERSELF_ADDRESS(-100028, "只能删除自己的地址！"),
    NOT_DEFAULT_ADDRESS(-100029, "请在我的->收货地址中设置默认收获地址！"),
    PASSWORD_NOT6(-100030, "密码不能少于6位"),
    NOT_ADMIN(-100031, "只有管理员才能登录后台"),
    ONLY_ADMIN_CAN_DO(-100032, "只有管理员才能进行操作！"),
    SEND_MESSAGE_ERROR(-100033, "发送短信验证码失败！"),
    AUTH_CODE_ERROR(-100034, "验证码错误！"),


    SUPPLIER_NOT_EXIST(-100035, "供应商不存在！"),
    GOODS_EXIST(-100036, "商品已存在!"),


    MESSAGE_NOT_EXIST(-100037, "消息不存在！"),

    ORDER_NOT_EXIST(-100038, "订单不存在"),

    FORMAT_TIME_ERROR(-100039, "传入时间格式错误！"),

    DELIVERY_PHONE_CONNOT_NULL(-100040, "配送员手机号不能为空！"),
    DELIVERY_NAME_CONNOT_NULL(-100041, "配送员姓名不能为空！"),
    DELIVERY_PASSWORD_CONNOT_NULL(-100042, "配送员密码不能为空！"),
    DELIVERY_DID_CONNOT_NULL(-100043, "配送员ID不能为空！"),
    DELIVERY_NOT_EXIST(-100044, "配送员不存在！"),

    NOE_USER_BING_THIS_WECHAT(-100045, "没有用户绑定该微信，请登录后进行绑定！"),

    DEL_MESSAGE_ERROR(-100046, "删除消息失败！"),
    ADDRESS_ID_CONNOT_NULL(-100047, "收货地址主键不能为空！"),

    MENU_NOT_FIND(-100048, "找不到菜单"),

    BUY_CAR_IS_NOT_YOUR(-100049, "订单无法对应本人！"),

    PLEASE_SELECTED_GOODS(-100050, "请选择商品"),

    CLASS_NAME_CONNOT_NULL(-100051, "分类名称不能为空"),

    ALREADY_ADD_FAVORITE(-100052, "您已经添加过该收藏了"),

    PASSWORD_ERROR(-100053, "密码错误！"),


    UPLOAD_FILE_TYPE_ERROR(-100054, "上传文件类型错误."),
    LOAD_FILE_FAILED(-100055, "加载文件失败"),
    ;


    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

    ErrEumn(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

}
