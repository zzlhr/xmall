import {Effect} from "dva";
import {notification} from 'antd'
import {Reducer} from "redux";
import {getGoodsCategory} from "@/pages/Goods/GoodsCategory/service";
import {GoodsCategoryVO} from "@/pages/Goods/GoodsCategory/data";


export interface CategoryStateType {
  categories: Array<GoodsCategoryVO>
}

export interface UserListModelType {
  namespace: string,
  state: CategoryStateType,
  effects: {
    fetchCategoryList: Effect,
  },
  reducers: {
    updateGoodsCategory: Reducer,
  }
}


const Model: UserListModelType = {
  namespace: 'goodsCategory',
  state: {
    categories: [],
  },
  effects: {
    * fetchCategoryList({payload}, {call, put}) {
      const response = yield call(getGoodsCategory, payload);
      if (response.code === 0) {
        yield put({
          type: 'updateGoodsCategory',
          payload: response.data
        })
      }
      if (response.code !== 0) {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    }
  },
  reducers: {
    updateGoodsCategory(state, {payload}) {
      return {
        ...state,
        categories: payload,
      }
    },
  }
};

export default Model;
