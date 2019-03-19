const httpUtil = {
    baseurl(name) {
        const host = "http://localhost:8008/";
        // const baseHost = host + "api/";
        const baseHost = "http://localhost:8008/"
        switch (name) {
            case 'home':
                return baseHost;
            case 'user':
                return baseHost + 'user/';
            case 'enterprise':
                return baseHost + 'enterprise/';
            case 'auth':
                return baseHost + 'auth/';
            case 'goods':
                return baseHost + 'goods/';
            case 'order':
                return baseHost + 'order/';
            case 'app':
                return baseHost + 'app/';
            case 'supplier':
                return baseHost + 'supplier/';
            case 'message':
                return baseHost + 'message/';
            case 'delivery':
                return baseHost + 'delivery/';
            case 'statistics':
                return baseHost + 'statistics/'
            default:
                return baseHost
        }
    },
    home() {
        return "http://localhost:8088/";
    },

    get(that, apiPath, url, callback) {
        console.log(this.baseurl(apiPath) + url);
        that.$http.get(this.baseurl(apiPath) + url).then(response => {
            if (response.status !== 200) {
                that.$notify.error({
                    title: '失败',
                    message: '错误代码:' + response.status
                        + '服务器未正常处理请求！',
                });
                return;
            } else if (response.body.code !== 0) {
                that.$notify.error({
                    title: '失败',
                    message: '错误代码:' + response.body.code
                        + response.body.msg,
                });
                return;
            }
            callback(response)
        }, response => {
            that.$notify.error({
                title: '失败',
                message: '错误代码:' + response.status
                    + '与服务器建立连接失败！',
            });
            // callback(response)
        })
    },
    post(that, apiPath, url, param, callback) {
        if (param.token === undefined) {
            param.token = that.$store.getters.user.token;
            param.e = that.$store.getters.user.enterprise;
        }
        that.$http.post(this.baseurl(apiPath) + url, param, {emulateJSON: true})
            .then(response => {
                if (response.status !== 200) {
                    that.$notify.error({
                        title: '失败',
                        message: '错误代码:' + response.status
                            + '服务器未正常处理请求！',
                    });
                    return;
                } else if (response.body.code !== 0) {
                    that.$notify.error({
                        title: '失败',
                        message: '错误代码:' + response.body.code
                            + response.body.msg,
                    });
                    return;
                }
                callback(response)
            }, response => {
                that.$notify.error({
                    title: '失败',
                    message: '错误代码:' + response.status
                        + '与服务器建立连接失败！',
                });
                // callback(response)
            })
    },

    getToken(that) {
        this.isLogin(that);
    },

    isLogin(that) {
        const token = that.$cookie.get("token");
        if (token === null) {
            that.$route.push({path: "/"});
            that.$message.error("请先登录")
        }
    }


};

export default httpUtil;
