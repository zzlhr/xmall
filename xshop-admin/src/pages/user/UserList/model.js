import {getAuthGroups, queryUsers} from "@/pages/user/UserList/service";
import {notification} from 'antd'

const Model = {
  namespace: "userList",
  state: {},
  effects: {

    * queryUsers({payload}, {call, put}) {
      const response = yield call(queryUsers, payload);
      if (response.code === 0) {
        return response;
      } else {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    },

    * getAuthGroups({payload}, {call, put}){
      const response = yield call(getAuthGroups);
      if (response.code === 0){
        return response;
      } else {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    }
  },
  reducers: {
  },
};

export default Model;
