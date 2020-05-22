import { Effect } from 'dva';
import { notification } from 'antd';
import { Reducer } from 'redux';
import { getGoodsDetail, getGoodsList, saveGoodsMaster } from '@/pages/Goods/GoodsList/service';
import { GoodsListVO, TableSearchData } from '@/pages/Goods/GoodsList/data';
import { TablePaginationConfig } from 'antd/lib/table';

export interface GoodsListStateType {
  tableSearchData: TableSearchData; // 表格查询条件
  goods: Array<GoodsListVO>;
  pagination: TablePaginationConfig;
}

export interface UserListModelType {
  namespace: string;
  state: GoodsListStateType;
  effects: {
    fetchGoodsList: Effect;
    saveGoodsMaster: Effect;
    getGoodsDetail: Effect;
  };
  reducers: {
    updateTableSearchData: Reducer;
    updateGoods: Reducer;
    updatePagination: Reducer;
  };
}

const Model: UserListModelType = {
  namespace: 'goods',
  state: {
    tableSearchData: {},
    goods: [], // 表格的数据
    pagination: {
      total: 0,
      current: 1,
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: (total: number) => `共${total}条`,
    },
  },
  effects: {
    *fetchGoodsList(_, { call, put, select }) {
      const search = yield select((state: any) => state.goods.tableSearchData);
      const pagination = yield select((state: any) => state.goods.pagination);

      const params = { ...search, page: pagination.current, pageSize: pagination.pageSize };

      const response = yield call(getGoodsList, params);
      if (response.code === 0) {
        yield put({
          type: 'updateGoods',
          payload: response.data,
        });
      }
      if (response.code !== 0) {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    },
    *saveGoodsMaster({ payload }, { call }) {
      const response = yield call(saveGoodsMaster, payload);

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

    *getGoodsDetail({ payload }, { call }) {
      const response = yield call(getGoodsDetail, payload);
      if (response.code === 0) {
        payload.callback(response.data);
      } else {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    },
  },
  reducers: {
    updateTableSearchData(state, { payload }) {
      return {
        ...state,
        tableSearchData: payload.tableSearchData,
      };
    },
    updateGoods(state, { payload }) {
      return {
        ...state,
        goods: payload.arr,
        pagination: {
          ...state.pagination,
          current: payload.currentPage,
          total: payload.totalCount,
          showTotal: (total: number) => `共${total}条`,
        },
      };
    },
    updatePagination(state, { payload }) {
      return {
        ...state,
        pagination: payload,
      };
    },
  },
};

export default Model;
