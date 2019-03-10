const Validate = {

    /* 只允许输入字母数字下划线 */
    checkPass:(rule, value, callback) => {
        let reg = /^[a-zA-Z0-9_]{1,}$/;
        console.log("进入了~~~~",value,value.match(reg));
        if ( !value.match(reg) ) {
            callback(new Error('密码只能为字母数字下划线'));
        } else {
            callback();
        }
    },
    /* 不能包含空格 */
    checkSpace:(rule, value, callback) => {
        if ( value.indexOf(' ') >= 0 ) {
            callback(new Error('不允许出现空格'));
        } else {
            callback();
        }
    },
    /* 不能包含非法字符 */
    checkIllegal:(rule, value, callback) => {
        let res = /[@#\$%\^&\*]+/g.test(value);
        if ( res ) {
            callback(new Error('不允许出现非法字符'));
        } else {
            callback();
        }
    }


}



export default Validate;