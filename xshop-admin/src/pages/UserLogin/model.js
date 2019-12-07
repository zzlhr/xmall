import { routerRedux } from 'dva/router';
import { adminLogin, getFakeCaptcha } from './service';
import { getPageQuery, setAuthority } from './utils/utils';
import { notification } from 'antd';
const Model = {
  namespace: 'userLogin',
  state: {
    status: undefined,
  },
  effects: {
    *login({ payload }, { call, put }) {
      const response = yield call(adminLogin, payload);
      yield put({
        type: 'changeLoginStatus',
        payload: response,
      }); // Login successfully
      console.log(response);
      // if (response.code !== 0){
      //
      //   return;
      // }
      if (response.code === 0) {
        const urlParams = new URL(window.location.href);
        const params = getPageQuery();
        let { redirect } = params;

        if (redirect) {
          const redirectUrlParams = new URL(redirect);

          if (redirectUrlParams.origin === urlParams.origin) {
            redirect = redirect.substr(urlParams.origin.length);

            if (redirect.match(/^\/.*#/)) {
              redirect = redirect.substr(redirect.indexOf('#') + 1);
            }
          } else {
            window.location.href = redirect;
            return;
          }
        }

        yield put(routerRedux.replace(redirect || '/'));
      }
    },

    *getCaptcha({ payload }, { call }) {
      yield call(getFakeCaptcha, payload);
    },
  },
  reducers: {
    changeLoginStatus(state, { payload }) {
      console.log(payload);
      console.log(state);
      if (payload.code !== 0){
        state.status = "error";
        notification.error({
          message: `请求错误 ${payload.code}`,
          description: payload.msg,
        });
      }else{
        // 成功设置权限
        console.log("登录成功！");
        setAuthority(payload.data);
      }
      return { ...state, status: payload.status, type: payload.type };
    },
  },
};
export default Model;
