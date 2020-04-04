import {Effect} from "dva";
import {PaginationConfig} from "antd/es/pagination";
import {notification} from 'antd'
// import {saveClient, findClients} from './service'
import {Reducer} from "redux";
import {SearchUserData, User} from "@/pages/user/UserList/data";
import {addUser, editUser, fetchUserList, phoneIsExist} from "@/pages/user/UserList/service";


export interface UserListStateType {
  tableSearchData: SearchUserData, // 表格查询条件
  users: Array<User>,
  pagination: PaginationConfig
}


export interface UserListModelType {
  namespace: string,
  state: UserListStateType,
  effects: {
    fetchUserList: Effect,
    addUser: Effect,
    editUser: Effect,
    phoneIsExist: Effect,
  },
  reducers: {
    updateTableSearchData: Reducer,
    updateUsers: Reducer,
    updateLoading: Reducer,
    updatePagination: Reducer,
  }
}


const Model: UserListModelType = {
  namespace: 'userList',
  state: {
    tableSearchData: {},
    users: [],// 表格的数据
    pagination: {
      total: 0,
      current: 1,
      showSizeChanger: true,
      showQuickJumper: true,
      // showTotal: () => `共${total}条`,
    }
  },
  effects: {
    * fetchUserList(_, {call, put, select}) {
      const search = yield select((state: any) => state.userList.tableSearchData);
      const pagination = yield select((state: any) => state.userList.pagination);

      const params = {...search, page: pagination.current, pageSize: pagination.pageSize};

      const response = yield call(fetchUserList, params);
      if (response.code === 0) {
        yield put({
          type: 'updateUsers',
          payload: response.data
        })
      }
      if (response.code !== 0) {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    },
    * addUser({payload}, {call, put, select}) {
      const response = yield call(addUser, payload);

      if (response.code === 0) {
        notification.error({
          message: `恭喜您`,
          description: `添加用户成功!`,
        });
        // TODO: 刷新数据
      } else {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }

    },
    * editUser({payload}, {call}) {
      const response = yield call(editUser, payload);
      if (response.code === 0) {
        notification.success({
          message: `成功`,
          description: '保存用户成功',
        });
      }
      if (response.code !== 0) {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    },
    * phoneIsExist({payload}, {call}) {
      const response = yield call(phoneIsExist, payload);
      if (response.code === 0) {
        // TODO: 处理结果
      }
      if (response.code !== 0) {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    },
  },
  reducers: {
    updateTableSearchData(state, {payload}) {
      return {
        ...state,
        tableSearchData: payload.tableSearchData,
      }
    },
    updateUsers(state, {payload}) {
      return {
        ...state,
        users: payload.arr,
        pagination: {
          ...state.pagination,
          current: payload.currentPage,
          total: payload.totalCount
        }
      }
    },
    updateLoading(state, {payload}) {
      return {
        ...state,
        clientsLoading: payload.clientsLoading
      }
    },
    updatePagination(state, {payload}) {
      return {
        ...state,
        pagination: payload
      }
    }
  }
};

export default Model;
