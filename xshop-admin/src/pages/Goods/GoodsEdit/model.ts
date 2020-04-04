import {Effect} from "dva";
import {notification} from 'antd'
import {getGoodsCategory, getGoodsDetail} from "@/pages/Goods/GoodsEdit/service";
import {GoodsCategory} from "@/pages/Goods/GoodsList/data";
import {Reducer} from "redux";
import {saveGoodsMaster} from "@/pages/Goods/GoodsList/service";


export interface GoodsEditStateType {
  goodsCategory: Array<GoodsCategory>
}

export interface UserListModelType {
  namespace: string,
  state: GoodsEditStateType,
  effects: {
    getGoodsDetail: Effect,
    getGoodsCategory: Effect,
    saveGoodsMaster: Effect,
  },
  reducers: {
    updateGoodsCategory: Reducer
  }
}


const Model: UserListModelType = {
  namespace: 'goodsEdit',
  state: {
    goodsCategory: []
  },
  effects: {
    * getGoodsDetail({payload}, {call}) {
      const response = yield call(getGoodsDetail, payload);
      if (response.code === 0) {
        payload.callback(response.data)
      } else {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    },
    * getGoodsCategory({payload}, {call, put}) {
      const response = yield call(getGoodsCategory, payload);
      if (response.code === 0) {
        yield put({
          type: 'updateGoodsCategory',
          payload: response.data
        })
      } else {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    },

    *saveGoodsMaster ({payload}, {call, put}){
      const response = yield call(saveGoodsMaster, payload);
      if (response.code === 0) {
        // yield put({
        //   type: 'updateGoodsCategory',
        //   payload: response.data
        // })
      } else {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
        });
      }
    }
  },
  reducers: {
    updateGoodsCategory(state, {payload}){
      return {
        ...state,
        goodsCategory: payload
      }
    }
  }
};

export default Model;
